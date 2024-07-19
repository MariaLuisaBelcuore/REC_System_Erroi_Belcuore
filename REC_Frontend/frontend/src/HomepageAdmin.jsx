import React, {useEffect, useState} from 'react';
import './CSS/HomepageAdmin.css';
import { useNavigate } from 'react-router-dom';
import {format} from "date-fns";

const HomepageAdmin = () => {
    const [apigClient, setApigClient] = useState(null);
    const navigate = useNavigate();
    const [userData, setUserData] = useState(null);
    const [error, setError] = useState('');
    const [performanceData, setPerformanceData] = useState(null);
    const [performanceError, setPerformanceError] = useState('');
    const [reportData, setReportData] = useState([]);
    const [reportError, setReportError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [adminEmail, setAdminEmail] = useState('');
    const [dateRange, setDateRange] = useState({
        startDate: '',
        endDate: '',
    });
    const [reward, setReward] = useState({
        name: '',
        description: '',
        cost: '',
        creationDate: '',
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
        performancesOptions: false,
        calculateResult: false,
        reportOfPeriodForm: false,
        reportResult: false,
        sendAdminCodeForm: false,
        createRewardForm: false,
        manageRECMenuOpen: false,
        updateForm: false,
    });


    useEffect(() => {
        setDisplayContent({
            welcome: true,
            accountInfo: false,
            performancesOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            sendAdminCodeForm: false,
            createRewardForm: false,
            manageRECMenuOpen: false,
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
            performancesOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            sendAdminCodeForm: false,
            createRewardForm: false,
            manageRECMenuOpen: false,
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
                        performancesOptions: false,
                        calculateResult: false,
                        reportOfPeriodForm: false,
                        reportResult: false,
                        sendAdminCodeForm: false,
                        createRewardForm: false,
                        manageRECMenuOpen: false,
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

        apigClient.apiManagementDeleteForAdminEmailDelete(params, body, additionalParams)
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
        const email = localStorage.getItem('email');
        updateData.ruolo = userData.ruolo;



        var params = {
            email: email,
            id: id,
            Authorization: `Bearer ${token}`
        };
        var body = {...updateData};
        var additionalParams = {};

        apigClient.apiManagementUpdateForAdminIdPatch(params, body, additionalParams)
            .then(function(result){
                alert('Account updated successfully');
                setUserData(result.data);
                setDisplayContent({
                    welcome: false,
                    accountInfo: true,
                    performancesOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    sendAdminCodeForm: false,
                    createRewardForm: false,
                    manageRECMenuOpen: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error updating account:', error);
            setError(error.message || 'Failed to update account');
        });
    };

        const handlePerformancesClick = () => {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                performancesOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        };

        const handleCalculateClick = async () => {
            const token = localStorage.getItem('jwt');
            const email = localStorage.getItem('email');


            var params = {
                Authorization: `Bearer ${token}`
            };
            var body = {};
            var additionalParams = {};

            apigClient.apiPerformanceCalculatePost(params, body, additionalParams)
                .then(function(result){
                    setPerformanceData(result.data);
                    setDisplayContent({
                        welcome: false,
                        accountInfo: false,
                        performancesOptions: false,
                        calculateResult: true,
                        reportOfPeriodForm: false,
                        reportResult: false,
                        sendAdminCodeForm: false,
                        createRewardForm: false,
                        manageRECMenuOpen: false,
                        updateForm: false,
                    });
                }).catch( function(result){
                console.error('Error calculating performance REC:', error);
                setPerformanceError(error.message || 'Failed to calculate performance REC');
            });
        };

        const handleReportOfPeriodClick = () => {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                performancesOptions: false,
                calculateResult: false,
                reportOfPeriodForm: true,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        };


        const handleGenerateReportClick = async () => {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('jwt');
            const {startDate, endDate} = dateRange;

            const formattedStartDate = format(new Date(startDate), "yyyy-MM-dd'T'HH:mm:ss");
            const formattedEndDate = format(new Date(endDate), "yyyy-MM-dd'T'HH:mm:ss");


            var params = {
                calculationDateStart: formattedStartDate,
                calculationDateEnd: formattedEndDate,
                Authorization: `Bearer ${token}`
            };
            var body = {};
            var additionalParams = {};

            apigClient.apiPerformanceReportOfPeriodGet(params, body, additionalParams)
                .then(function(result){
                    setReportData(result.data.list);
                    setDisplayContent({
                        welcome: false,
                        accountInfo: false,
                        performancesOptions: false,
                        calculateResult: false,
                        reportOfPeriodForm: false,
                        reportResult: true,
                        sendAdminCodeForm: false,
                        createRewardForm: false,
                        manageRECMenuOpen: false,
                        updateForm: false,
                    });
                }).catch( function(result){
                console.error('Error generating report:', error);
                setReportError(error.message || 'Failed to generate report');
            });
        };


        const handleDateRangeChange = (e) => {
            const {name, value} = e.target;
            setDateRange((prevState) => ({
                ...prevState,
                [name]: value,
            }));
        };

    const handleSendAdminCodeClick = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            performancesOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            sendAdminCodeForm: true,
            createRewardForm: false,
            manageRECMenuOpen: false,
            updateForm: false,
        });
    };

    const handleSendCode = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('jwt');

        const emailDTO = {
            ownerRef: adminEmail,
            emailFrom: '',
            emailTo: adminEmail,
            subject: '',
            text: '',
        };

        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {...emailDTO};
        var additionalParams = {};

        apigClient.apiEmailSendInvitationPost(params, body, additionalParams)
            .then(function(result){
                alert('Admin code sent successfully!');
                setAdminEmail('');
                setDisplayContent({
                    welcome: true,
                    accountInfo: false,
                    performancesOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    sendAdminCodeForm: false,
                    createRewardForm: false,
                    manageRECMenuOpen: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error sending admin code:', error);
            setError('Failed to send admin code');
        });
    };

    const handleCreateRewardClick = () => {
        setDisplayContent({
            welcome: false,
            accountInfo: false,
            performancesOptions: false,
            calculateResult: false,
            reportOfPeriodForm: false,
            reportResult: false,
            sendAdminCodeForm: false,
            createRewardForm: true,
            manageRECMenuOpen: false,
            updateForm: false,
        });
    };

    const handleRewardChange = (e) => {
        const { name, value } = e.target;
        setReward((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleCreateReward = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('jwt');
        const rewardDTO = {
            ...reward,
            creationDate: new Date().toISOString(),
        };


        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {...rewardDTO};
        var additionalParams = {};

        apigClient.apiRewardCreatePost(params, body, additionalParams)
            .then(function(result){
                alert('Reward created successfully');
                setDisplayContent({
                    welcome: true,
                    accountInfo: false,
                    performancesOptions: false,
                    calculateResult: false,
                    reportOfPeriodForm: false,
                    reportResult: false,
                    sendAdminCodeForm: false,
                    createRewardForm: false,
                    manageRECMenuOpen: false,
                    updateForm: false,
                });
            }).catch( function(result){
            console.error('Error creating reward:', error);
            setError(error.message || 'Failed to create reward');
        });
    };

    const toggleManageRECMenu = () => {
        setDisplayContent((prevState) => ({
            ...prevState,
            manageRECMenuOpen: !prevState.manageRECMenuOpen,
        }));
    };

    const handleManageRECItemClick = (itemName) => {
        // Definisci la navigazione per ciascun elemento del menu Manage REC
        switch (itemName) {
            case 'Tasks':
                navigate('/tasks'); // Sostituisci con il percorso della tua pagina JSX per Tasks
                break;
            case 'Emails':
                navigate('/emails'); // Sostituisci con il percorso della tua pagina JSX per Emails
                break;
            case 'Energy Resources':
                navigate('/energy-resources'); // Sostituisci con il percorso della tua pagina JSX per Energy Resources
                break;
            case 'Wallets':
                navigate('/wallets'); // Sostituisci con il percorso della tua pagina JSX per Wallets
                break;
            case 'Payments':
                navigate('/payments'); // Sostituisci con il percorso della tua pagina JSX per Payments
                break;
            case 'Users':
                navigate('/users'); // Sostituisci con il percorso della tua pagina JSX per Users
                break;
            case 'Rewards':
                navigate('/rewards'); // Sostituisci con il percorso della tua pagina JSX per Rewards
                break;
            default:
                break;
        }

        // Chiudi il menu Manage REC dopo la selezione
        setDisplayContent((prevState) => ({
            ...prevState,
            manageRECMenuOpen: false,
        }));
    };


    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.updateForm) {
            setDisplayContent({
                welcome: false,
                accountInfo: true,
                performancesOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.createRewardForm) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                performancesOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.sendAdminCodeForm) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                performancesOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.reportResult) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                performancesOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.reportOfPeriodForm) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                performancesOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.calculateResult) {
            setDisplayContent({
                welcome: false,
                accountInfo: false,
                performancesOptions: true,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.performancesOptions) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                performancesOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        } else if (displayContent.accountInfo) {
            setDisplayContent({
                welcome: true,
                accountInfo: false,
                performancesOptions: false,
                calculateResult: false,
                reportOfPeriodForm: false,
                reportResult: false,
                sendAdminCodeForm: false,
                createRewardForm: false,
                manageRECMenuOpen: false,
                updateForm: false,
            });
        }
    };



    return (
            <div className="home-container">
                <header className="top-bar">
                    <img src={`${process.env.PUBLIC_URL}/Logo.png`} alt="Logo" className="logo"/>
                    <a href="#!" className="logout-link" onClick={handleLogout}>Logout</a>
                </header>
                <div className="content">
                    <nav className="sidebar">
                        <ul className="menu">
                            <li className="menu-item"><a href="#!" onClick={handleBackClick}>Back</a></li>
                            <li className="menu-item"><a href="#!" onClick={handleAccountClick}>Account</a></li>
                            <li className="menu-item"><a href="#" onClick={handleCreateRewardClick}>Create Reward</a>
                            </li>
                            <li className="menu-item"><a href="#!" onClick={handlePerformancesClick}>Performances
                                REC</a></li>
                            <li className="menu-item"><a href="#!" onClick={handleSendAdminCodeClick}>Send Admin
                                Code</a></li>
                            <li className="menu-item"><a href="#!" onClick={() => toggleManageRECMenu('Manage REC')}
                                                         className="manage-rec-button">Manage REC</a>
                                {displayContent.manageRECMenuOpen && (
                                    <ul className="manage-rec-menu">
                                        <li><a href="#" onClick={() => handleManageRECItemClick('Tasks')}>Tasks</a></li>
                                        <li><a href="#" onClick={() => handleManageRECItemClick('Emails')}>Emails</a>
                                        </li>
                                        <li><a href="#" onClick={() => handleManageRECItemClick('Energy Resources')}>Energy
                                            Resources</a></li>
                                        <li><a href="#" onClick={() => handleManageRECItemClick('Wallets')}>Wallets</a>
                                        </li>
                                        <li><a href="#"
                                               onClick={() => handleManageRECItemClick('Payments')}>Payments</a></li>
                                        <li><a href="#" onClick={() => handleManageRECItemClick('Users')}>Users</a></li>
                                        <li><a href="#" onClick={() => handleManageRECItemClick('Rewards')}>Rewards</a>
                                        </li>
                                    </ul>
                                )}
                            </li>
                        </ul>
                    </nav>
                    <main className="main-content">
                    {displayContent.welcome && (
                            <>
                                <h1>Welcome to Admin Dashboard</h1>
                                <p>Here you can manage the system.</p>
                            </>
                        )}
                        {displayContent.accountInfo && userData && (
                            <div>
                                <div>
                                    <h1>Admin Information</h1>
                                    <p><strong>ID:</strong> {userData.id}</p>
                                    <p><strong>Nome:</strong> {userData.nome}</p>
                                    <p><strong>Cognome:</strong> {userData.cognome}</p>
                                    <p><strong>Email:</strong> {userData.email}</p>
                                    <p><strong>Ruolo:</strong> {userData.ruolo}</p>
                                    <button onClick={handleDeleteAccount}>Delete</button>
                                    <button onClick={handleUpdateAccount}>Update</button>
                                </div>
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
                        {displayContent.performancesOptions && (
                            <>
                                <h1>Performances Options</h1>
                                <button onClick={handleCalculateClick}>Calculate</button>
                                <button onClick={handleReportOfPeriodClick}>Report of Period</button>
                            </>
                        )}
                        {displayContent.calculateResult && performanceData && (
                            <div>
                                <h1>Performances REC</h1>
                                <p><strong>ID:</strong> {performanceData.id}</p>
                                <p><strong>Calculation Date:</strong> {performanceData.calculationDate}</p>
                                <p><strong>Energy Resold (%) :</strong> {performanceData.energyResoldPercentage}</p>
                                <p><strong>Total Earnings:</strong> {performanceData.totalEarnings}</p>
                                <p><strong>Total Energy Available:</strong> {performanceData.totalEnergyAvailable}</p>
                                <p><strong>Total Energy Consumed:</strong> {performanceData.totalEnergyConsumed}</p>
                            </div>
                        )}
                        {displayContent.reportOfPeriodForm && (
                            <>
                                <h1>Report of Period</h1>
                                <label>Start Date:</label>
                                <input type="datetime-local" name="startDate" value={dateRange.startDate}
                                       onChange={handleDateRangeChange} required/><br/>
                                <label>End Date:</label>
                                <input type="datetime-local" name="endDate" value={dateRange.endDate}
                                       onChange={handleDateRangeChange} required/><br/>
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
                                        <p><strong>Energy Resold (%) :</strong> {performance.energyResoldPercentage}
                                        </p>
                                        <p><strong>Total Earnings:</strong> {performance.totalEarnings}</p>
                                        <p><strong>Total Energy
                                            Available:</strong> {performance.totalEnergyAvailable}</p>
                                        <p><strong>Total Energy Consumed:</strong> {performance.totalEnergyConsumed}
                                        </p>
                                        <hr/>
                                    </div>
                                ))}
                            </>
                        )}
                        {displayContent.sendAdminCodeForm && (
                            <>
                                <h1>Send Admin Code</h1>
                                <form onSubmit={handleSendCode}>
                                    <label>Email:</label>
                                    <input
                                        type="email"
                                        value={adminEmail}
                                        onChange={(e) => setAdminEmail(e.target.value)}
                                        required
                                    /><br />
                                    <button type="submit">SEND</button>
                                </form>
                            </>
                        )}
                        {displayContent.createRewardForm && (

                            <>
                                <h1>Create Reward</h1>
                                <label>Name:</label>
                                <input type="text" name="name" value={reward.name} onChange={handleRewardChange} required /><br />
                                <label>Description:</label>
                                <input type="text" name="description" value={reward.description} onChange={handleRewardChange} required /><br />
                                <label>Cost:</label>
                                <input type="number" name="cost" value={reward.cost} onChange={handleRewardChange} required /><br />
                                <button onClick={handleCreateReward}>Create</button>
                                {error && <p className="error-message">{error}</p>}
                            </>
                        )}
                        {performanceError && <p className="error-message">{performanceError}</p>}
                        {error && <p className="error-message">{error}</p>}

                    </main>
                </div>
            </div>
        );
    };

    export default HomepageAdmin;
