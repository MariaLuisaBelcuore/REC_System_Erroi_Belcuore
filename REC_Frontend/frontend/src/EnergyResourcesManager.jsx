import React, { useEffect, useState } from 'react';
import './CSS/EnergyResourcesManager.css'; // Importa il file CSS per le personalizzazioni

const EnergyResourcesManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [memberEmail, setMemberEmail] = useState('');
    const [resources, setResources] = useState([]);
    const [selectedResource, setSelectedResource] = useState(null);
    const [error, setError] = useState('');

    const [resourceData, setResourceData] = useState({
        description: '',
        owner: '',
        memberEmail: '',
        typeEnergy: '',
        availableTime: 0,
        kWh: 0,
        os: '',
        memory: 0,
        processorModel: '',
        processorVelocity: 0,
        availability: false,
    });

    const [displayContent, setDisplayContent] = useState({
        welcome: true,
        resourcesList: false,
        update: false,
    });

    useEffect(() => {
        setDisplayContent({
            welcome: true,
            resourcesList: false,
            update: false,
        });
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);

    const handleEmailChange = (e) => {
        setMemberEmail(e.target.value);
    };

    const handleSearchClick = async () => {
        const token = localStorage.getItem('jwt');

        var params = {
            memberEmail: memberEmail,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiEnergyResourcesSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setResources(result.data.list);
                setSelectedResource(null);
                setDisplayContent({
                    welcome: false,
                    resourcesList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error fetching resources:', error);
            setError(error.message || 'Failed to fetch resources');
        });
    };

    const handleCheckboxChange = (resource) => {
        setSelectedResource(resource);
    };

    const handleDeleteClick = async () => {
        const token = localStorage.getItem('jwt');
        const resourceId = selectedResource.id;


        var params = {
            id: resourceId,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiEnergyResourcesDeleteIdDelete(params, body, additionalParams)
            .then(function(result){
                alert('Resource deleted successfully');
                const updatedResources = resources.filter((resource) => resource.id !== resourceId);
                setResources(updatedResources);
                setSelectedResource(null);
            }).catch( function(result){
            console.error('Error deleting resource:', error);
            setError(error.message || 'Failed to delete resource');
        });
    };

    const handleUpdateClick = () => {
        setDisplayContent({
            welcome: false,
            resourcesList: false,
            update: true,
        });
    };

    const handleApplyClick = async () => {
        const token = localStorage.getItem('jwt');
        const resourceId = selectedResource.id;
        resourceData.memberEmail = selectedResource.memberEmail;


        var params = {
            id: resourceId,
            Authorization: `Bearer ${token}`
        };
        var body = {...resourceData};
        var additionalParams = {};

        apigClient.apiEnergyResourcesUpdateIdPatch(params, body, additionalParams)
            .then(function(result){
                alert('Resource updated successfully');

                setDisplayContent({
                    welcome: false,
                    resourcesList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error updating resource:', error);
            setError(error.message || 'Failed to update resource');
        });
    };

    const handleResourceInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        setResourceData((prevState) => ({
            ...prevState,
            [name]: type === 'checkbox' ? checked : value,
        }));
    };

    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.update) {
            setDisplayContent({
                welcome: false,
                resourcesList: true,
                update: false,
            });
        } else if (displayContent.resourcesList) {
            setDisplayContent({
                welcome: true,
                resourcesList: false,
                update: false,
            });
        }
    };

    return (
        <div className="home-container">
            <header className="top-bar-manager">
                <button onClick={handleBackClick} className="back-button">Back</button>
                <img src={`${process.env.PUBLIC_URL}/Logo.png`} alt="Logo" className="logo"/>
            </header>
            <main className="main-content">
                {displayContent.welcome && (
                    <>
                        <h1>Energy Resource Manager</h1>
                        <label htmlFor="member-email">Member Email:</label>
                        <input
                            type="email"
                            id="member-email"
                            value={memberEmail}
                            onChange={handleEmailChange}
                        />
                        <button onClick={handleSearchClick}>SEARCH</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.resourcesList && resources.length > 0 && (
                    <div className="resources-list">
                        <h2>Resources List</h2>
                        <ul>
                            {resources.map((resource) => (
                                <li
                                    key={resource.id}
                                    className={`resources-item ${selectedResource && selectedResource.id === resource.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(resource)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedResource && selectedResource.id === resource.id}
                                        onChange={() => handleCheckboxChange(resource)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {resource.id}</p>
                                        <p><strong>Description:</strong> {resource.description}</p>
                                        <p><strong>Owner:</strong> {resource.owner}</p>
                                        <p><strong>Member Email:</strong> {resource.memberEmail}</p>
                                        <p><strong>Type Energy:</strong> {resource.typeEnergy}</p>
                                        <p><strong>Available Time:</strong> {resource.availableTime}</p>
                                        <p><strong>kWh:</strong> {resource.kWh}</p>
                                        <p><strong>OS:</strong> {resource.os}</p>
                                        <p><strong>Memory:</strong> {resource.memory}</p>
                                        <p><strong>Processor Model:</strong> {resource.processorModel}</p>
                                        <p><strong>Processor Velocity:</strong> {resource.processorVelocity} GHz</p>
                                        <p><strong>Availability:</strong> {resource.availability ? 'Yes' : 'No'}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="resources-actions">
                            <button onClick={handleDeleteClick} className="action-button">DELETE</button>
                            <button onClick={handleUpdateClick} className="action-button">UPDATE</button>
                        </div>
                    </div>
                )}
                {displayContent.update && (
                    <>
                        <h2>Update Resource</h2>
                        <label>Description:</label>
                        <input
                            type="text"
                            name="description"
                            value={resourceData.description}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Owner:</label>
                        <input
                            type="text"
                            name="owner"
                            value={resourceData.owner}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Type Energy:</label>
                        <input
                            type="text"
                            name="typeEnergy"
                            value={resourceData.typeEnergy}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Available Time:</label>
                        <input
                            type="number"
                            name="availableTime"
                            value={resourceData.availableTime}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>kWh:</label>
                        <input
                            type="number"
                            name="kWh"
                            value={resourceData.kWh}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>OS:</label>
                        <input
                            type="text"
                            name="os"
                            value={resourceData.os}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Memory:</label>
                        <input
                            type="number"
                            name="memory"
                            value={resourceData.memory}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Processor Model:</label>
                        <input
                            type="text"
                            name="processorModel"
                            value={resourceData.processorModel}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Processor Velocity:</label>
                        <input
                            type="number"
                            name="processorVelocity"
                            value={resourceData.processorVelocity}
                            onChange={handleResourceInputChange}
                            required
                        /><br />
                        <label>Availability:</label>
                        <input
                            type="checkbox"
                            name="availability"
                            checked={resourceData.availability}
                            onChange={handleResourceInputChange}
                        /><br />
                        <button onClick={handleApplyClick}>Apply</button>
                    </>
                )}
            </main>
        </div>
    );
};

export default EnergyResourcesManager;
