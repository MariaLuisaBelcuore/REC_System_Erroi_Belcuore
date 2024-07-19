import React, { useEffect, useState } from 'react';
import './CSS/HomepageMember.css';
import { format } from 'date-fns';
import { useNavigate } from 'react-router-dom';

const HomepageMember = () => {
    const [apigClient, setApigClient] = useState(null);
    const navigate = useNavigate();
    const [userData, setUserData] = useState(null);
    const [error, setError] = useState('');
    const [walletData, setWalletData] = useState(null);
    const [walletError, setWalletError] = useState('');
    const [resourceData, setResourceData] = useState([]);
    const [resourceError, setResourceError] = useState('');
    const [performanceData, setPerformanceData] = useState(null);
    const [performanceError, setPerformanceError] = useState('');
    const [reportData, setReportData] = useState([]);
    const [reportError, setReportError] = useState('');
    const [rewards, setRewards] = useState([]);
    const [selectedReward, setSelectedReward] = useState(null);
    const [rewardError, setRewardError] = useState('');
    const [rewardspurchased, setRewardspurchased] = useState([]);
    const [selectedRewardpurchased, setSelectedRewardpurchased] = useState(null);
    const [rewardpurchasedError, setRewardpurchasedError] = useState('');
    const [movements, setMovements] = useState([]);
    const [movementsError, setMovementsError] = useState('');
    const [dateRange, setDateRange] = useState({
        startDate: '',
        endDate: '',
    });
    const [newWallet, setNewWallet] = useState({
        nome: '',
        cognome: '',
    });
    const [newResource, setNewResource] = useState({
        description: '',
        owner: '',
        memberEmail: localStorage.getItem('email'),
        typeEnergy: '',
        availableTime: 0,
        kWh: 0,
        os: '',
        memory: 0,
        processorModel: '',
        processorVelocity: 0,
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
        resourceList: false,
        energySoldOptions: false,
        calculateResult: false,
        reportOfPeriodForm: false,
        reportResult: false,
        addEnergyResourceForm: false,
        viewPurchasedRewards: false,
        viewMovements: false,
        updateForm: false,
    });

    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        setDisplayContent({
            welcome: true,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            resourceList: false,
            energySoldOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            addEnergyResourceForm: false,
            viewPurchasedRewards: false,
            viewMovements: false,
            updateForm: false,
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
            resourceList: false,
            energySoldOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            addEnergyResourceForm: false,
            viewPurchasedRewards: false,
            viewMovements: false,
            updateForm: true,
        });
    };

    const handleUpdateInputChange = (e) => {
        const { name, value } = e.target;
        setUpdateData(prevState => ({
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
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    viewMovements: false,
                    updateForm: false,
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
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    viewMovements: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error updating account:', error);
            setError(error.message || 'Failed to update account');
        });
    };

    const handlePaymentMethodClick = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            memberEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiWalletMemberSearchGet(params, body, additionalParams)
            .then(function(result){
                setWalletData(result.data);
                console.log(result.data)
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: true,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    viewMovements: false,
                    updateForm: false,
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
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                viewMovements: false,
                updateForm: false,
            });
        });
    };

    const handleViewMovements = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');

        var params = {
            memberEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiPaymentMemberSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setMovements(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    viewMovements: true,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching movements:', error);
            setMovementsError(error.message || 'Failed to fetch movements');
        });
    };



    const handleMyResourcesClick = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');



        var params = {
            memberEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiEnergyResourcesSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setResourceData(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    resourceList: true,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching resources:', error);
            setResourceError(error.message || 'Failed to fetch resources');
        });
    };

    const handleEnergySoldClick = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            resourceList: false,
            energySoldOptions: true,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            addEnergyResourceForm: false,
            viewPurchasedRewards: false,
            updateForm: false,
        });
    };

    const handleCalculateClick = async () => {
        const token = localStorage.getItem('jwt');
        const email = localStorage.getItem('email');


        var params = {
            memberEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiPerformanceMemberCalculatePost(params, body, additionalParams)
            .then(function(result){
                console.log(result.data)
                setPerformanceData(result.data);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: true,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error calculating energy sold:', error);
            setPerformanceError(error.message || 'Failed to calculate energy sold');
        });
    };

    const handleReportOfPeriodClick = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            resourceList: false,
            energySoldOptions: false,
            calculateResult: false,
            reportOfPeriodForm: true,
            reportResult: false,
            addEnergyResourceForm: false,
            viewPurchasedRewards: false,
            updateForm: false,
        });
    };

    const handleGenerateReportClick = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');
        const { startDate, endDate } = dateRange;

        const formattedStartDate = format(new Date(startDate), "yyyy-MM-dd'T'HH:mm:ss");
        const formattedEndDate = format(new Date(endDate), "yyyy-MM-dd'T'HH:mm:ss");



        var params = {
            memberEmail: email,
            Authorization: `Bearer ${token}`,
            calculationDateStart: formattedStartDate,
            calculationDateEnd: formattedEndDate
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiPerformanceMemberReportOfPeriodGet(params, body, additionalParams)
            .then(function(result){
                setReportData(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: true,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error generating report:', error);
            setReportError(error.message || 'Failed to generate report');
        });
    };

    const handleBuyRewardsClick = async () => {
        const token = localStorage.getItem('jwt');


        var params = {
            searchTerm: '',
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiRewardSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setRewards(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    buyRewards: true,
                    viewPurchasedRewards: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching rewards:', error);
            setRewardError(error.message || 'Failed to fetch rewards');
        });
    };

    const handleRewardSelect = (reward) => {
        setSelectedReward(reward);
    };

    const handleBuySelectedReward = async () => {
        if (!selectedReward) {
            alert("Please select a reward to buy.");
            return;
        }

        const token = localStorage.getItem('jwt');
        const email = localStorage.getItem('email');


        const rewardPurchasedDTO = {
            id: selectedReward.id,
            emailMember: email,
            rewardId: selectedReward.id,
            purchaseDate: new Date().toISOString(),
            used: false,
            cost: selectedReward.cost,
        };


        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {...rewardPurchasedDTO};
        var additionalParams = {};

        apigClient.apiRewardPurchasedCreatePost(params, body, additionalParams)
            .then(function(result){
                alert("Reward purchased successfully!");
                setSelectedReward(null);
            }).catch( function(result){
            console.error('Error buying reward:', error);
            setRewardError(error.message || 'Failed to buy reward');
        });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewWallet((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleDateRangeChange = (e) => {
        const { name, value } = e.target;
        setDateRange((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };


    const handleViewRewardsPurchasedClick = async () => {
        const token = localStorage.getItem('jwt');
        const email = localStorage.getItem('email');



        var params = {
            memberEmail: email,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiRewardPurchasedSearchGet(params, body, additionalParams)
            .then(function(result){
                setRewardspurchased(result.data.list);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: false,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    buyRewards: false,
                    viewPurchasedRewards: true,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error fetching rewards purchased:', error);
            setRewardpurchasedError(error.message || 'Failed to fetch rewards purchased');
        });
    };


    const handleRewardPurchasedSelect = (rewardPurchased) => {
        setSelectedRewardpurchased(rewardPurchased);
    };


    const handleUseSelectedRewardPurchased = async () => {
        if (!selectedRewardpurchased) {
            alert("Please select a reward to use.");
            return;
        }

        const token = localStorage.getItem('jwt');
        const email = localStorage.getItem('email');
        const id = selectedRewardpurchased.id;

        var params = {
            id: id,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiRewardPurchasedUseIdPatch(params, body, additionalParams)
            .then(function(result){
                alert("Reward used successfully!");
                setSelectedRewardpurchased(null);
            }).catch( function(result){
            console.error('Error using reward:', error);
            setRewardpurchasedError(error.message || 'Failed to use reward');
        });
    };

    const handleSaveWallet = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');



        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {
            memberEmail: email,
            nome: newWallet.nome,
            cognome: newWallet.cognome,
            residualCredit: 1000,
        };
        var additionalParams = {};

        apigClient.apiWalletMemberCreatePost(params, body, additionalParams)
            .then(function(result){
                alert('wallet created successfully');
                setWalletData(result.data);
                setDisplayContent({
                    welcome: false,
                    accountInfo: false,
                    paymentMethodInfo: true,
                    createWalletForm: false,
                    resourceList: false,
                    energySoldOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    addEnergyResourceForm: false,
                    viewPurchasedRewards: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error creating wallet:', error);
            setWalletError(error.message || 'Failed to create wallet');
        });
    };

    const handleAddEnergyResourceClick = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            paymentMethodInfo: false,
            createWalletForm: false,
            resourceList: false,
            energySoldOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            addEnergyResourceForm: true,
            viewPurchasedRewards: false,
            updateForm: false,
        });
    };

    const handleCreateResource = async () => {
        const email = localStorage.getItem('email');
        const token = localStorage.getItem('jwt');


        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {...newResource};
        var additionalParams = {};

        apigClient.apiEnergyResourcesCreatePost(params, body, additionalParams)
            .then(function(result){
                alert('resource created successfully');
            }).catch( function(result){
            console.error('Error creating resource:', error);
        });
    };

    const handleResourceInputChange = (e) => {
        const { name, value } = e.target;
        setNewResource((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.updateForm) {
            setDisplayContent({
                welcome: false,
                accountInfo: true,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.viewPurchasedRewards) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.addEnergyResourceForm) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.reportResult) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.reportOfPeriodForm) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.calculateResult) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.energySoldOptions) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.resourceList) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.createWalletForm) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.paymentMethodInfo) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
            });
        } else if (displayContent.accountInfo) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                paymentMethodInfo: false,
                createWalletForm: false,
                resourceList: false,
                energySoldOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                addEnergyResourceForm: false,
                viewPurchasedRewards: false,
                updateForm: false,
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
                        <li className="menu-item"><a href="#!" onClick={handleAddEnergyResourceClick}>Add Energy
                            Resource</a></li>
                        <li className="menu-item"><a href="#!" onClick={handleMyResourcesClick}>My Resources</a></li>
                        <li className="menu-item"><a href="#!" onClick={handlePaymentMethodClick}>Payment Method</a>
                        </li>
                        <li className="menu-item"><a href="#!" onClick={handleBuyRewardsClick}>Buy Rewards</a></li>
                        <li className="menu-item"><a href="#!" onClick={handleEnergySoldClick}>Energy Sold</a></li>
                        <li className="menu-item"><a href="#!" onClick={handleViewRewardsPurchasedClick}>My Rewards</a>
                        </li>
                    </ul>
                </nav>
                <main className="main-content">
                {displayContent.welcome && (
                        <>
                            <h1>Welcome to Member Dashboard</h1>
                            <p>Here you can manage your account, add energy resources, view your resources, manage payment method, and buy rewards.</p>
                        </>
                    )}
                    {displayContent.accountInfo && userData && (
                        <div>
                            <h1>Member Information</h1>
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
                            <p><strong>Email:</strong> {walletData.memberEmail}</p>
                            <p><strong>Residual Credit:</strong> {walletData.residualCredit}</p>
                            <button onClick={handleViewMovements}>Movements</button>
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
                                            <p><strong>Type:</strong> {movement.paymentorcredit}</p>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p>No movements found.</p>
                            )}
                            {movementsError && <p className="error-message">{movementsError}</p>}
                        </>
                    )}
                    {displayContent.createWalletForm && (
                        <>
                            <h1>Create New Payment Method</h1>
                            <label>Name:</label>
                            <input type="text" name="nome" value={newWallet.nome} onChange={handleInputChange} required /><br />
                            <label>Surname:</label>
                            <input type="text" name="cognome" value={newWallet.cognome} onChange={handleInputChange} required /><br />
                            <button onClick={handleSaveWallet}>Save</button>
                            {walletError && <p className="error-message">{walletError}</p>}
                        </>
                    )}
                    {displayContent.resourceList && (
                        <>
                            <h1>My Resources</h1>
                            {resourceData.length > 0 ? (
                                <ul>
                                    {resourceData.map((resource) => (
                                        <li key={resource.id}>
                                            <p><strong>ID:</strong> {resource.id}</p>
                                            <p><strong>Description:</strong> {resource.description}</p>
                                            <p><strong>Owner:</strong> {resource.owner}</p>
                                            <p><strong>Type of Energy:</strong> {resource.typeEnergy}</p>
                                            <p><strong>Available Time:</strong> {resource.availableTime}</p>
                                            <p><strong>kWh:</strong> {resource.kWh}</p>
                                            <p><strong>OS:</strong> {resource.os}</p>
                                            <p><strong>Memory:</strong> {resource.memory}</p>
                                            <p><strong>Processor Model:</strong> {resource.processorModel}</p>
                                            <p><strong>Processor Velocity:</strong> {resource.processorVelocity}</p>
                                            <p><strong>Availability:</strong> {resource.availability ? 'Available' : 'Not Available'}</p>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p>No resources found.</p>
                            )}
                            {resourceError && <p className="error-message">{resourceError}</p>}
                        </>
                    )}
                    {displayContent.energySoldOptions && (
                        <>
                            <h1>Energy Sold Options</h1>
                            <button onClick={handleCalculateClick}>Calculate</button>
                            <button onClick={handleReportOfPeriodClick}>Report of Period</button>
                        </>
                    )}
                    {displayContent.calculateResult && performanceData && (
                        <div>
                            <h1>Energy Resold</h1>
                            <p><strong>ID:</strong> {performanceData.id}</p>
                            <p><strong>Calculation Date:</strong> {performanceData.calculationDate}</p>
                            <p><strong>Total Energy Available:</strong> {performanceData.totalEnergyAvailable}</p>
                            <p><strong>Total Energy:</strong> {performanceData.totalEnergy}</p>
                            <p><strong>Energy Resold:</strong> {performanceData.energyResold}</p>
                        </div>
                    )}
                    {displayContent.reportOfPeriodForm && (
                        <>
                            <h1>Report of Period</h1>
                            <label>Start Date:</label>
                            <input type="datetime-local" name="startDate" value={dateRange.startDate} onChange={handleDateRangeChange} required /><br />
                            <label>End Date:</label>
                            <input type="datetime-local" name="endDate" value={dateRange.endDate} onChange={handleDateRangeChange} required /><br />
                            <button onClick={handleGenerateReportClick}>Generate Report</button>
                        </>
                    )}
                    {displayContent.reportResult && reportData.length > 0 && (
                        <>
                            <h1>Report of Period</h1>
                            {reportData.map((performance) => (
                                <div key={performance.id}>
                                    <p><strong>ID:</strong> {performance.id}</p>
                                    <p><strong>Calculation Date:</strong> {performance.calculationDate}</p>
                                    <p><strong>Total Energy Available:</strong> {performance.totalEnergyAvailable}</p>
                                    <p><strong>Total Energy:</strong> {performance.totalEnergy}</p>
                                    <p><strong>Energy Resold:</strong> {performance.energyResold}</p>
                                    <hr />
                                </div>
                            ))}
                        </>
                    )}
                    {displayContent.addEnergyResourceForm && (
                        <>
                            <h2>Add Energy Resource</h2>
                            <label>Description:</label>
                            <input
                                type="text"
                                name="description"
                                value={newResource.description}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>Owner:</label>
                            <input
                                type="text"
                                name="owner"
                                value={newResource.owner}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>Type of Energy:</label>
                            <input
                                type="text"
                                name="typeEnergy"
                                value={newResource.typeEnergy}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>Available Time:</label>
                            <input
                                type="number"
                                name="availableTime"
                                value={newResource.availableTime}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>kWh:</label>
                            <input
                                type="number"
                                name="kWh"
                                value={newResource.kWh}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>OS:</label>
                            <input
                                type="text"
                                name="os"
                                value={newResource.os}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>Memory:</label>
                            <input
                                type="number"
                                name="memory"
                                value={newResource.memory}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>Processor Model:</label>
                            <input
                                type="text"
                                name="processorModel"
                                value={newResource.processorModel}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <label>Processor Velocity:</label>
                            <input
                                type="number"
                                name="processorVelocity"
                                value={newResource.processorVelocity}
                                onChange={handleResourceInputChange}
                                required
                            /><br />
                            <button onClick={handleCreateResource}>Create</button>
                        </>
                    )}
                    {displayContent.buyRewards && (
                        <>
                            <h1>Buy Rewards</h1>
                            {rewardError && <p className="error-message">{rewardError}</p>}
                            <ul>
                                {rewards.map((reward) => (
                                    <li key={reward.id}>
                                        <input
                                            type="radio"
                                            name="reward"
                                            value={reward.id}
                                            onChange={() => handleRewardSelect(reward)}
                                        />
                                        <strong>Name:</strong> {reward.name}<br />
                                        <strong>Description:</strong> {reward.description}<br />
                                        <strong>Cost:</strong> {reward.cost}
                                    </li>
                                ))}
                            </ul>
                            <button onClick={handleBuySelectedReward}>BUY</button>
                        </>
                    )}
                    {displayContent.viewPurchasedRewards && (
                        <>
                            <h1>Use Rewards</h1>
                            {rewardpurchasedError && <p className="error-message">{rewardpurchasedError}</p>}
                            <ul>
                                {rewardspurchased.map((rewardpurchased) => (
                                    <li key={rewardpurchased.id}>
                                        <input
                                            type="radio"
                                            name="rewardpurchased"
                                            value={rewardpurchased.id}
                                            onChange={() => handleRewardPurchasedSelect(rewardpurchased)}
                                        />
                                        <p><strong>Id:</strong> {rewardpurchased.id}</p>
                                        <p><strong>Name:</strong> {rewardpurchased.name}</p>
                                        <p><strong>Description:</strong> {rewardpurchased.description}</p>
                                    </li>
                                ))}
                            </ul>
                            <button onClick={handleUseSelectedRewardPurchased}>USE</button>
                        </>
                    )}
                    {performanceError && <p className="error-message">{performanceError}</p>}
                    {reportError && <p className="error-message">{reportError}</p>}
                    {successMessage && <p className="success-message">{successMessage}</p>}
                    {resourceError && <p className="error-message">{resourceError}</p>}
                </main>
            </div>
        </div>
    );
};

export default HomepageMember;

