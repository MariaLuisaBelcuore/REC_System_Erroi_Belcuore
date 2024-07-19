import React, { useEffect, useState } from 'react';
import './CSS/RewardsManager.css'; // Importa il file CSS per le personalizzazioni

const RewardsManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [rewards, setRewards] = useState([]);
    const [selectedReward, setSelectedReward] = useState(null);
    const [error, setError] = useState('');

    const [rewardData, setRewardData] = useState({
        name: '',
        description: '',
        cost: 0,
        creationDate: '',
    });

    const [displayContent, setDisplayContent] = useState({
        welcome: true,
        rewardsList: false,
        update: false,
    });

    useEffect(() => {
        setDisplayContent({
            welcome: true,
            rewardsList: false,
            update: false,
        });
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);

    const handleSearchTermChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleSearchClick = async () => {
        const token = localStorage.getItem('jwt');


        var params = {
            searchTerm:'',
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiRewardSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setRewards(result.data.list);
                setSelectedReward(null);
                setDisplayContent({
                    welcome: false,
                    rewardsList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error fetching rewards:', error);
            setError(error.message || 'Failed to fetch rewards');
        });
    };

    const handleCheckboxChange = (reward) => {
        setSelectedReward(reward);
    };

    const handleDeleteClick = async () => {
        const token = localStorage.getItem('jwt');
        const rewardId = selectedReward.id;

        var params = {
            id: rewardId,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiRewardDeleteIdDelete(params, body, additionalParams)
            .then(function(result){
                const updatedRewards = rewards.filter((reward) => reward.id !== rewardId);
                setRewards(updatedRewards);
                setSelectedReward(null);
            }).catch( function(result){
            console.error('Error deleting reward:', error);
            setError(error.message || 'Failed to delete reward');
        });
    };

    const handleUpdateClick = () => {
        setDisplayContent({
            welcome: false,
            rewardsList: false,
            update: true,
        });
    };

    const handleApplyClick = async () => {
        const token = localStorage.getItem('jwt');
        const rewardId = selectedReward.id;
        rewardData.creationDate = selectedReward.creationDate;


        var params = {
            id: rewardId,
            Authorization: `Bearer ${token}`
        };
        var body = {...rewardData};
        var additionalParams = {};

        apigClient.apiRewardUpdateIdPatch(params, body, additionalParams)
            .then(function(result){
                alert('Reward updated successfully');
                setDisplayContent({
                    welcome: false,
                    rewardsList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error updating reward:', error);
            setError(error.message || 'Failed to update reward');
        });
    };

    const handleRewardInputChange = (e) => {
        const { name, value } = e.target;
        setRewardData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.update) {
            setDisplayContent({
                welcome: false,
                rewardsList: true,
                update: false,
            });
        } else if (displayContent.rewardsList) {
            setDisplayContent({
                welcome: true,
                rewardsList: false,
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
                        <h1>Rewards Manager</h1>
                        <button onClick={handleSearchClick}>SEARCH</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.rewardsList && rewards.length > 0 && (
                    <div className="rewards-list">
                        <h2>Rewards List</h2>
                        <ul>
                            {rewards.map((reward) => (
                                <li
                                    key={reward.id}
                                    className={`rewards-item ${selectedReward && selectedReward.id === reward.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(reward)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedReward && selectedReward.id === reward.id}
                                        onChange={() => handleCheckboxChange(reward)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {reward.id}</p>
                                        <p><strong>Name:</strong> {reward.name}</p>
                                        <p><strong>Description:</strong> {reward.description}</p>
                                        <p><strong>Cost:</strong> {reward.cost}</p>
                                        <p><strong>Creation Date:</strong> {reward.creationDate}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="rewards-actions">
                            <button onClick={handleDeleteClick} className="action-button">DELETE</button>
                            <button onClick={handleUpdateClick} className="action-button">UPDATE</button>
                        </div>
                    </div>
                )}
                {displayContent.update && (
                    <>
                        <h2>Update Reward</h2>
                        <label>Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={rewardData.name}
                            onChange={handleRewardInputChange}
                            required
                        /><br />
                        <label>Description:</label>
                        <input
                            type="text"
                            name="description"
                            value={rewardData.description}
                            onChange={handleRewardInputChange}
                            required
                        /><br />
                        <label>Cost:</label>
                        <input
                            type="number"
                            name="cost"
                            value={rewardData.cost}
                            onChange={handleRewardInputChange}
                            required
                        /><br />
                        <button onClick={handleApplyClick}>Apply</button>
                    </>
                )}
            </main>
        </div>
    );
};

export default RewardsManager;
