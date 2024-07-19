import React, { useState, useEffect } from 'react';
import './CSS/HomepageClient.css';
import { useNavigate } from 'react-router-dom';

const HomepageClient = () => {
    const [apigClient, setApigClient] = useState(null);
    const navigate = useNavigate();
    const [userData, setUserData] = useState(null);
    const [error, setError] = useState('');
    const [walletData, setWalletData] = useState(null);
    const [walletError, setWalletError] = useState('');
    const [taskHistory, setTaskHistory] = useState(null);
    const [taskError, setTaskError] = useState('');
    const [movements, setMovements] = useState([]);
    const [movementsError, setMovementsError] = useState('');
    const [resourceList, setResourceList] = useState([]);
    const [selectedResource, setSelectedResource] = useState(null);
    const [successMessage, setSuccessMessage] = useState('');
    const [newWallet, setNewWallet] = useState({
        nome: '',
        cognome: '',
        cardNumber: '',
        cardDeadline: '',
        cvc: '',
    });

    const [updateWallet, setUpdateWallet] = useState({
        nome: '',
        cognome: '',
        clientEmail: localStorage.getItem('email'),
        cardNumber: '',
        cardDeadline: '',
        cvc: '',
        residualCredit: 0
    });
    const [taskData, setTaskData] = useState({
        name: '',
        description: '',
        executionTime: '',
        linkGit: '',
        os: '',
        memory: '',
        processorModel: '',
        processorVelocity: '',
        clientEmail: localStorage.getItem('email')
    });

    const [updateData, setUpdateData] = useState({
        nome: '',
        cognome: '',
        email: localStorage.getItem('email'),
        password: '',
        ruolo: '',
    });

    const [displayContent, setDisplayContent] = useState({
        welcome: true,
        accountInfo: false,
        paymentMethodInfo: false,
        createWalletForm: false,
        taskForm: false,
        resourceList: false,
        taskHistory: false,
        viewMovements: false,
        updateForm: false,
        updateCartForm: false,
    });


    useEffect(() => {
        setDisplayContent({
            welcome: true,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            taskForm: false,
            resourceList: false,
            taskHistory: false,
            viewMovements: false,
            updateForm: false,
            updateCartForm: false,
        });
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);

    const handleUpdateAccount = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            taskForm: false,
            resourceList: false,
            taskHistory: false,
            viewMovements: false,
            updateForm: true,
            updateCartForm: false,
        });
    };

    const handleUpdateInputChange = (e) => {
        const { name, value } = e.target;
        setUpdateData(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleUpdateCartInputChange = (e) => {
        const { name, value } = e.target;
        setUpdateWallet(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleLogout = () => {
        localStorage.removeItem('email');
        localStorage.removeItem('jwt');
        navigate('/');
    };

    const handleAccountClick = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            email: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiManagementSearchEmailGet(params, body, additionalParams)
            .then(function(result){
                setUserData(result.data);
                setDisplayContent({
                    welcome: false,
                    accountInfo: true,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: false,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching user data:', error);
            setError(error.message || 'Failed to fetch user data');
        });

    };

    const handleDeleteAccount = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');

        var params = {
            email: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiManagementDeleteEmailDelete(params, body, additionalParams)
            .then(function(result){
                alert('account deleted successfully');
                navigate('/');
            }).catch( function(result){
            console.error('Error deleting account:', error);
            setError(error.message || 'Failed to delete account');
        });
    };

    const handleApplyUpdateAccount = async () => {
        const id = userData.id;
        const token = localStorage.getItem('jwt');
        updateData.ruolo = userData.ruolo;


        var params = {
            id: id,
            Authorization: `Bearer ${token}`
        };
        var body = {...updateData};
        var additionalParams = {};

        apigClient.apiManagementUpdateIdPatch(params, body, additionalParams)
            .then(function(result){
                alert('Account updated successfully');
                const updatedUserData = result.data;
                setUserData(updatedUserData);
                setDisplayContent({
                    welcome: false,
                    accountInfo: true,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: false,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error updating account:', error);
            setError(error.message || 'Failed to update account');
        });
    };

    const handleViewTaskHistoryClick = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            clientEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiTaskSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setTaskHistory(result.data.list);
                console.log(result.data)
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: true,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching task history:', error);
            setTaskError(error.message || 'Failed to fetch task history');
        });
    };


    const handlePaymentMethodClick = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            clientEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiWalletClientSearchGet(params, body, additionalParams)
            .then(function(result){
                console.log(result.data)
                setWalletData(result.data);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: true,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: false,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching wallet data:', error);
            setWalletError(error.message || 'Failed to fetch wallet data');
            setWalletData(null);
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: true,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        });
    };


    const handleViewMovements = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            clientEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiPaymentClientSearchAllByClientGet(params, body, additionalParams)
            .then(function(result){
                setMovements(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: false,
                    viewMovements: true,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching movements:', error);
            setMovementsError(error.message || 'Failed to fetch movements');
        });
    };

    const handleUpdateCart = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');
        updateWallet.nome = walletData.nome
        updateWallet.cognome = walletData.cognome
        updateWallet.residualCredit = walletData.residualCredit

        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {...updateWallet};
        var additionalParams = {};

        apigClient.apiWalletClientUpdatePatch(params, body, additionalParams)
            .then(function(result){
                setWalletData(result.data);
                console.log(result.data)
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: true,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: false,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error update cart:', error);
            setMovementsError(error.message || 'Failed to update cart');
        });
    };

    const handleUpdateCartClick = async () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            taskForm: false,
            resourceList: false,
            taskHistory: false,
            viewMovements: false,
            updateForm: false,
            updateCartForm: true,
        });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewWallet(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleTaskInputChange = (e) => {
        const { name, value } = e.target;
        setTaskData(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSaveWallet = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');

        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {
            clientEmail: email,
            nome: newWallet.nome,
            cognome: newWallet.cognome,
            cardNumber: newWallet.cardNumber,
            cardDeadline: newWallet.cardDeadline,
            cvc: newWallet.cvc,
            residualCredit: 1000,
        };
        var additionalParams = {};

        apigClient.apiWalletClientCreatePost(params, body, additionalParams)
            .then(function(result){
                alert('wallet created successfully');
                setWalletData(result.data);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: true,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: false,
                    taskHistory: false,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error creating wallet:', error);
            setWalletError(error.message || 'Failed to create wallet');
        });
    };

    const handleSubmitTaskClick = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            taskForm: true,
            resourceList: false,
            taskHistory: false,
            viewMovements: false,
            updateForm: false,
            updateCartForm: false,
        });
    };

    const handleSearchResource = async () => {
        const { executionTime, os, memory, processorModel, processorVelocity } = taskData;
        const resourceRequestDTO = {
            availableTime: executionTime,
            os,
            memory,
            processorModel,
            processorVelocity: parseFloat(processorVelocity)
        };

        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {...resourceRequestDTO};
        var additionalParams = {};

        apigClient.apiEnergyResourcesSearchAllForTaskPost(params, body, additionalParams)
            .then(function(result){
                setResourceList(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    taskForm: false,
                    resourceList: true,
                    taskHistory: false,
                    viewMovements: false,
                    updateForm: false,
                    updateCartForm: false,
                });
            }).catch( function(result){
            console.error('Error searching resources:', error);
            setError(error.message || 'Failed to search resources');
        });
    };

    const handleResourceSelect = (id) => {
        setSelectedResource(id);
    };

    const handleSubmitTask = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            Authorization: `Bearer ${token}`};
        var body = {...taskData};
        var additionalParams = {};

        apigClient.apiTaskCreatePost(params, body, additionalParams)
            .then(function(result){
                alert('Task created successfully');
                var params = {
                    id: selectedResource,
                    executionTime: taskData.executionTime,
                    Authorization: `Bearer ${token}`
                };
                var body = {};
                var additionalParams = {};

                apigClient.apiEnergyResourcesUpdateTimeIdPatch(params, body, additionalParams)
                    .then(function(result){
                    }).catch( function(result){
                    console.error('Error update time:', error);
                    setError(error.message || 'Failed to update time');
                });
            }).catch( function(result){
            console.error('Error submitting task:', error);
            setError(error.message || 'Failed to submit task');
        });
    };


    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.updateForm) {
            setDisplayContent({
                welcome: false,
                accountInfo: true,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.viewMovements) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: true,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.taskHistory) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.resourceList) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: true,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.taskForm) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.createWalletForm) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.paymentMethodInfo) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        } else if (displayContent.accountInfo) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        }else if (displayContent.updateCartForm) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: true,
                createWalletForm: false,
                taskForm: false,
                resourceList: false,
                taskHistory: false,
                viewMovements: false,
                updateForm: false,
                updateCartForm: false,
            });
        }
    };

    return (
        <div className="home-container">
            <header className="top-bar">
                <img src={`${process.env.PUBLIC_URL}/Logo.png`} alt="Logo" className="logo" />
                <a href="#!" className="logout-link" onClick={handleLogout}>Logout</a>
            </header>
            <div className="content">
                <nav className="sidebar">
                    <ul className="menu">
                        <li className="menu-item"><a href="#!" onClick={handleBackClick}>Back</a></li>
                        <li className="menu-item"><a href="#!" onClick={handleAccountClick}>Account</a></li>
                        <li className="menu-item"><a href="#!" onClick={handlePaymentMethodClick}>Payment Method</a>
                        </li>
                        <li className="menu-item"><a href="#!" onClick={handleSubmitTaskClick}>Submit a Task</a></li>
                        <li className="menu-item"><a href="#!" onClick={handleViewTaskHistoryClick}>View Task
                            History</a></li>
                    </ul>
                </nav>
                <main className="main-content">
                {displayContent.welcome && (
                        <>
                            <h1>Welcome to Client Dashboard</h1>
                            <p>Here you can manage your account, submit tasks, and view your task history.</p>
                        </>
                    )}
                    {displayContent.accountInfo && userData && (
                        <div>
                            <h1>User Information</h1>
                            <p><strong>ID:</strong> {userData.id}</p>
                            <p><strong>Nome:</strong> {userData.nome}</p>
                            <p><strong>Cognome:</strong> {userData.cognome}</p>
                            <p><strong>Email:</strong> {userData.email}</p>
                            <p><strong>Ruolo:</strong> {userData.ruolo}</p>
                            <button onClick={handleDeleteAccount}>Delete</button>
                            <button onClick={handleUpdateAccount}>Update</button>
                        </div>
                    )}
                    {displayContent.updateForm && (
                        <>
                            <h1>Update Account</h1>
                            <label>Nome:</label>
                            <input type="text" name="nome" value={updateData.nome} onChange={handleUpdateInputChange} required /><br />
                            <label>Cognome:</label>
                            <input type="text" name="cognome" value={updateData.cognome} onChange={handleUpdateInputChange} required /><br />
                            <label>Password:</label>
                            <input type="password" name="password" value={updateData.password} onChange={handleUpdateInputChange} required /><br />
                            <button onClick={handleApplyUpdateAccount}>Apply</button>
                            {error && <p className="error-message">{error}</p>}
                        </>
                    )}
                    {displayContent.paymentMethodInfo && walletData && (
                        <div>
                            <h1>Payment Method Information</h1>
                            <p><strong>ID:</strong> {walletData.id}</p>
                            <p><strong>Nome:</strong> {walletData.nome}</p>
                            <p><strong>Cognome:</strong> {walletData.cognome}</p>
                            <p><strong>Email:</strong> {walletData.clientEmail}</p>
                            <p><strong>Card Number:</strong> {walletData.cardNumber}</p>
                            <p><strong>Card Deadline:</strong> {walletData.cardDeadline}</p>
                            <p><strong>CVC:</strong> {walletData.cvc}</p>
                            <p><strong>Residual Credit:</strong> {walletData.residualCredit}</p>
                            <button onClick={handleViewMovements}>Movements</button>
                            <button onClick={handleUpdateCartClick}>Change Cart</button>
                        </div>
                    )}
                    {displayContent.viewMovements && (
                        <>
                            <h1>My Movements</h1>
                            {movements.length > 0 ? (
                                <ul>
                                    {movements.map((movement) => (
                                        <li key={movement.id}>
                                            <p><strong>ID:</strong> {movement.id}</p>
                                            <p><strong>Causal:</strong> {movement.causal}</p>
                                            <p><strong>Date:</strong> {movement.emissionDate}</p>
                                            <p><strong>Amount:</strong> {movement.amount}</p>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p>No movements found.</p>
                            )}
                            {movementsError && <p className="error-message">{movementsError}</p>}
                        </>
                    )}
                    {displayContent.updateCartForm && (
                        <>
                            <h1>Update Wallet</h1>
                            <label>Card Number:</label>
                            <input type="number" name="cardNumber" value={updateWallet.cardNumber} onChange={handleUpdateCartInputChange} required /><br />
                            <label>Card Dead line:</label>
                            <input type="text" name="cardDeadline" value={updateWallet.cardDeadline} onChange={handleUpdateCartInputChange} required /><br />
                            <label>CVC:</label>
                            <input type="text" name="cvc" value={updateWallet.cvc} onChange={handleUpdateCartInputChange} required /><br />
                            <button onClick={handleUpdateCart}>Apply</button>
                            {error && <p className="error-message">{error}</p>}
                        </>
                    )}
                    {displayContent.createWalletForm && (
                        <>
                            <h1>Create New Payment Method</h1>
                            <label>Name:</label>
                            <input type="text" name="nome" value={newWallet.nome} onChange={handleInputChange} required /><br />
                            <label>Surname:</label>
                            <input type="text" name="cognome" value={newWallet.cognome} onChange={handleInputChange} required /><br />
                            <label>Card Number:</label>
                            <input type="text" name="cardNumber" value={newWallet.cardNumber} onChange={handleInputChange} required /><br />
                            <label>Card Deadline:</label>
                            <input type="text" name="cardDeadline" value={newWallet.cardDeadline} onChange={handleInputChange} required /><br />
                            <label>CVC:</label>
                            <input type="text" name="cvc" value={newWallet.cvc} onChange={handleInputChange} required /><br />
                            <button onClick={handleSaveWallet}>Save</button>
                            {walletError && <p className="error-message">{walletError}</p>}
                        </>
                    )}
                    {displayContent.taskHistory && taskHistory && (
                        <div>
                            <h1>Task History</h1>
                            {taskHistory.length > 0 ? (
                                taskHistory.map((task, index) => (
                                    <div key={task.id} style={{ marginBottom: '20px' }}>
                                        <p><strong>ID:</strong> {task.id}</p>
                                        <p><strong>Name:</strong> {task.name}</p>
                                        <p><strong>Description:</strong> {task.description}</p>
                                        <p><strong>Execution Time:</strong> {task.executionTime}</p>
                                        <p><strong>Link Git:</strong> {task.linkGit}</p>
                                        <p><strong>OS:</strong> {task.os}</p>
                                        <p><strong>Memory:</strong> {task.memory}</p>
                                        <p><strong>Processor Model:</strong> {task.processorModel}</p>
                                        <p><strong>Processor Velocity:</strong> {task.processorVelocity}</p>
                                        <p><strong>Client Email:</strong> {task.clientEmail}</p>
                                        {index < taskHistory.length - 1 && <hr style={{ margin: '20px 0' }} />}
                                    </div>
                                ))
                            ) : (
                                <p>No tasks found.</p>
                            )}
                        </div>
                    )}
                    {taskError && <p className="error-message">{taskError}</p>}
                    {displayContent.taskForm && (
                        <>
                            <h1>Submit a Task</h1>
                            <label>Name:</label>
                            <input type="text" name="name" value={taskData.name} onChange={handleTaskInputChange} required /><br />
                            <label>Description:</label>
                            <input type="text" name="description" value={taskData.description} onChange={handleTaskInputChange} required /><br />
                            <label>Execution Time:</label>
                            <input type="number" name="executionTime" value={taskData.executionTime} onChange={handleTaskInputChange} required /><br />
                            <label>Link Git:</label>
                            <input type="text" name="linkGit" value={taskData.linkGit} onChange={handleTaskInputChange} required /><br />
                            <label>OS:</label>
                            <input type="text" name="os" value={taskData.os} onChange={handleTaskInputChange} required /><br />
                            <label>Memory:</label>
                            <input type="number" name="memory" value={taskData.memory} onChange={handleTaskInputChange} required /><br />
                            <label>Processor Model:</label>
                            <input type="text" name="processorModel" value={taskData.processorModel} onChange={handleTaskInputChange} required /><br />
                            <label>Processor Velocity:</label>
                            <input type="number" name="processorVelocity" value={taskData.processorVelocity} onChange={handleTaskInputChange} required /><br />
                            <button onClick={handleSearchResource}>SEARCH RESOURCE</button>
                            {error && <p className="error-message">{error}</p>}
                        </>
                    )}
                    {displayContent.resourceList && (
                        <>
                            <h1>Resource List</h1>
                            {resourceList.length > 0 ? (
                                resourceList.map(resource => (
                                    <div key={resource.id}>
                                        <input
                                            type="checkbox"
                                            checked={selectedResource === resource.id}
                                            onChange={() => handleResourceSelect(resource.id)}
                                        />
                                        <p><strong>ID:</strong> {resource.id}</p>
                                        <p><strong>Description:</strong> {resource.description}</p>
                                        <p><strong>Owner:</strong> {resource.owner}</p>
                                        <p><strong>Member Email:</strong> {resource.memberEmail}</p>
                                        <p><strong>Type Energy:</strong> {resource.typeEnergy}</p>
                                        <p><strong>Available Time:</strong> {resource.availableTime}</p>
                                        <p><strong>kWh:</strong> {resource.kWh}</p>
                                        <p><strong>OS:</strong> {resource.os}</p>
                                        <p><strong>Memory:</strong> {resource.memory}</p>
                                        <p><strong>Processor Model:</strong> {resource.processorModel}</p>
                                        <p><strong>Processor Velocity:</strong> {resource.processorVelocity}</p>
                                        <p><strong>Availability:</strong> {resource.availability ? 'Yes' : 'No'}</p>
                                        <hr style={{ margin: '20px 0' }} />
                                    </div>
                                ))
                            ) : (
                                <p>No resources found.</p>
                            )}
                            <button onClick={handleSubmitTask}>SUBMIT</button>
                        </>
                    )}
                    {successMessage && <p className="success-message">{successMessage}</p>}
                </main>
            </div>
        </div>
    );
};

export default HomepageClient;
