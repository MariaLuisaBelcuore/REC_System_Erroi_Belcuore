import React, { useEffect, useState } from 'react';
import './CSS/WalletsManager.css';

const WalletsManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [wallets, setWallets] = useState([]);
    const [selectedWallet, setSelectedWallet] = useState(null);
    const [error, setError] = useState('');
    const [walletType, setWalletType] = useState(null);


    const [displayContent, setDisplayContent] = useState({
        selection: true,
        welcome: false,
        walletsList: false,
    });

    useEffect(() => {
        setDisplayContent({
            selection: true,
            welcome: false,
            walletsList: false,
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

        if (walletType === 'CLIENTS') {

            apigClient.apiWalletClientSearchAllGet(params, body, additionalParams)
                .then(function(result){
                    setWallets(result.data.list);
                    setSelectedWallet(null); // Resetta l'utente selezionato quando vengono caricati nuovi utenti
                    setDisplayContent({
                        selection: false,
                        welcome: false,
                        walletsList: true,
                    });
                }).catch( function(result){
                console.error('Error fetching wallet:', error);
                setError(error.message || 'Failed to fetch wallet');
            });
        } else if (walletType === 'MEMBERS') {

            apigClient.apiWalletMemberSearchAllGet(params, body, additionalParams)
                .then(function(result){
                    setWallets(result.data.list);
                    setSelectedWallet(null); // Resetta l'utente selezionato quando vengono caricati nuovi utenti
                    setDisplayContent({
                        selection: false,
                        welcome: false,
                        walletsList: true,
                    });
                }).catch( function(result){
                console.error('Error fetching wallet:', error);
                setError(error.message || 'Failed to fetch wallet');
            });
        }
    };

    const handleWalletTypeSelection = (type) => {
        setWalletType(type);
        setDisplayContent({
            selection: false,
            welcome: true,
            walletsList: false,
        });
    };

    const handleCheckboxChange = (wallet) => {
        setSelectedWallet(wallet);
    };

    const handleDeleteClientClick = async () => {
        const token = localStorage.getItem('jwt');
        const clientEmail = selectedWallet.clientEmail;


        var params = {
            clientEmail: clientEmail,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiWalletClientDeleteDelete(params, body, additionalParams)
            .then(function(result){
                alert('wallet deleted successfully');
                const updatedWallets = wallets.filter((wallet) => wallet.clientEmail !== clientEmail);
                setWallets(updatedWallets);
                setSelectedWallet(null);
            }).catch( function(result){
            console.error('Error deleting wallet:', error);
            setError(error.message || 'Failed to delete wallet');
        });
    };

    const handleDeleteMemberClick = async () => {
        const token = localStorage.getItem('jwt');
        const memberEmail = selectedWallet.memberEmail;

        var params = {
            memberEmail: memberEmail,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiWalletMemberDeleteDelete(params, body, additionalParams)
            .then(function(result){
                alert('wallet deleted successfully');
                const updatedWallets = wallets.filter((wallet) => wallet.memberEmail !== memberEmail);
                setWallets(updatedWallets);
                setSelectedWallet(null);
            }).catch( function(result){
            console.error('Error deleting wallet:', error);
            setError(error.message || 'Failed to delete wallet');
        });
    };


    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.walletsList) {
            setDisplayContent({
                selection: true,
                welcome: false,
                walletsList: false,
            });
        } else if (displayContent.welcome) {
            setDisplayContent({
                selection: true,
                welcome: false,
                walletsList: false,
            });
        }
    };

    return (
        <div className="home-container">
            <header className="top-bar-manager">
                <button onClick={handleBackClick} className="back-button">Back</button>

                <img src={`${process.env.PUBLIC_URL}/Logo.png`} alt="Logo" className="logo-manager"/>

            </header>
            <main className="main-content">
                {displayContent.selection && (
                    <>
                        <h1>Payments Manager</h1>
                        <button onClick={() => handleWalletTypeSelection('CLIENTS')}>CLIENTS</button>
                        <button onClick={() => handleWalletTypeSelection('MEMBERS')}>MEMBERS</button>
                    </>
                )}
                {displayContent.welcome && (
                    <>
                        <h1>Wallets Manager</h1>
                        <button onClick={handleSearchClick}>SEARCH</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.walletsList && wallets.length > 0 && (
                    <div className="wallets-list">
                        <h2>{walletType === 'CLIENTS' ? 'Clients Wallets List' : 'Members Wallets List'}</h2>
                        <ul>
                            {wallets.map((wallet) => (
                                <li
                                    key={wallet.id}
                                    className={`wallets-item ${selectedWallet && selectedWallet.id === wallet.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(wallet)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedWallet && selectedWallet.id === wallet.id}
                                        onChange={() => handleCheckboxChange(wallet)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {wallet.id}</p>
                                        <p><strong>Nome:</strong> {wallet.nome}</p>
                                        <p><strong>Cognome:</strong> {wallet.cognome}</p>
                                        <p><strong>Email:</strong> {walletType === 'CLIENTS' ? wallet.clientEmail : wallet.memberEmail}</p>
                                        <p><strong>Residual Credit:</strong> {wallet.residualCredit}</p>
                                        {walletType === 'CLIENTS' && (
                                            <>
                                                <p><strong>Card Number:</strong> {wallet.cardNumber}</p>
                                                <p><strong>Card Deadline:</strong> {wallet.cardDeadline}</p>
                                                <p><strong>CVC:</strong> {wallet.cvc}</p>
                                            </>
                                        )}
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="wallets-actions">
                            {walletType === 'CLIENTS' ? (
                                <button onClick={handleDeleteClientClick} className="action-button">DELETE</button>
                            ) : (
                                <button onClick={handleDeleteMemberClick} className="action-button">DELETE</button>
                            )}
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
};

export default WalletsManager;
