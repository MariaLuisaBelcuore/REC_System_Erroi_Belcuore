import React, { useEffect, useState } from 'react';
import './CSS/EmailsManager.css'; // Importa il file CSS per le personalizzazioni

const EmailsManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [clientEmail, setClientEmail] = useState('');
    const [emails, setEmails] = useState([]);
    const [selectedEmail, setSelectedEmail] = useState(null);
    const [error, setError] = useState('');

    const [displayContent, setDisplayContent] = useState({
        welcome: true,
        emailsList: false,
    });

    useEffect(() => {
        setDisplayContent({
            welcome: true,
            emailsList: false,
        });
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);


    const handleSearchClick = async () => {
        const token = localStorage.getItem('jwt');

        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiEmailSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setEmails(result.data.list);
                setSelectedEmail(null);
                setDisplayContent({
                    welcome: false,
                    emailsList: true,
                });
            }).catch( function(result){
            console.error('Error fetching emails:', error);
            setError(error.message || 'Failed to fetch emails');
        });
    };

    const handleCheckboxChange = (email) => {
        setSelectedEmail(email);
    };

    const handleDeleteClick = async () => {
        const token = localStorage.getItem('jwt');
        const emailId = selectedEmail.id;


        var params = {
            id: emailId,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiEmailDeleteIdDelete(params, body, additionalParams)
            .then(function(result){
                alert('Email deleted successfully');
                // Aggiorna la lista delle email dopo la cancellazione
                const updatedEmails = emails.filter((email) => email.id !== emailId);
                setEmails(updatedEmails);
                setSelectedEmail(null);
            }).catch( function(result){
            console.error('Error deleting email:', error);
            setError(error.message || 'Failed to delete email');
        });

    };

    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.emailsList) {
            setDisplayContent({
                welcome: true,
                emailsList: false,
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
                        <h1>Emails Manager</h1>
                        <button onClick={handleSearchClick}>SEARCH EMAILS</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.emailsList && emails.length > 0 && (
                    <div className="emails-list">
                        <h2>Emails List</h2>
                        <ul>
                            {emails.map((email) => (
                                <li
                                    key={email.id}
                                    className={`emails-item ${selectedEmail && selectedEmail.id === email.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(email)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedEmail && selectedEmail.id === email.id}
                                        onChange={() => handleCheckboxChange(email)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {email.id}</p>
                                        <p><strong>Owner Ref:</strong> {email.ownerRef}</p>
                                        <p><strong>Email From:</strong> {email.emailFrom}</p>
                                        <p><strong>Email To:</strong> {email.emailTo}</p>
                                        <p><strong>Subject:</strong> {email.subject}</p>
                                        <p><strong>Text:</strong> {email.text}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="emails-actions">
                            <button onClick={handleDeleteClick} className="action-button">DELETE</button>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
};

export default EmailsManager;
