import React, { useEffect, useState } from 'react';
import './CSS/PaymentsManager.css';

const PaymentsManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [payments, setPayments] = useState([]);
    const [selectedPayment, setSelectedPayment] = useState(null);
    const [error, setError] = useState('');
    const [paymentType, setPaymentType] = useState(null);
    const [memberEmail, setMemberEmail] = useState('');


    const [displayContent, setDisplayContent] = useState({
        selection: true,
        welcome: false,
        memberSearch: false,
        paymentsList: false,
    });

    useEffect(() => {
        setDisplayContent({
            selection: true,
            welcome: false,
            memberSearch: false,
            paymentsList: false,
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
        let url = '';


        if (paymentType === 'CLIENTS') {

            var params = {
                Authorization: `Bearer ${token}`
            };
            var body = {};
            var additionalParams = {};

            apigClient.apiPaymentClientSearchAllGet(params, body, additionalParams)
                .then(function(result){
                    setPayments(result.data.list);
                    setSelectedPayment(null);
                    setDisplayContent({
                        selection: false,
                        welcome: false,
                        memberSearch: false,
                        paymentsList: true,
                    });
                }).catch( function(result){
                console.error('Error fetching payments:', error);
                setError(error.message || 'Failed to fetch payments');
            });

        } else if (paymentType === 'MEMBERS') {

            var params = {
                memberEmail: memberEmail,
                Authorization: `Bearer ${token}`
            };
            var body = {};
            var additionalParams = {};

            apigClient.apiPaymentMemberSearchAllGet(params, body, additionalParams)
                .then(function(result){
                    setPayments(result.data.list);
                    setSelectedPayment(null);
                    setDisplayContent({
                        selection: false,
                        welcome: false,
                        memberSearch: false,
                        paymentsList: true,
                    });
                }).catch( function(result){
                console.error('Error fetching payments:', error);
                setError(error.message || 'Failed to fetch payments');
            });
        }
    };

    const handlePaymentTypeSelection = (type) => {
        setPaymentType(type);
        setDisplayContent({
            selection: false,
            welcome: type === 'CLIENTS',
            memberSearch: type === 'MEMBERS',
            paymentsList: false,
        });
    };

    const handleCheckboxChange = (payment) => {
        setSelectedPayment(payment);
    };

    const handleDeleteClientClick = async () => {
        const token = localStorage.getItem('jwt');
        const paymentId = selectedPayment.id;


        var params = {
            id: paymentId,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiPaymentClientDeleteIdDelete(params, body, additionalParams)
            .then(function(result){
                alert('payment deleted successfully');
                const updatedPayments = payments.filter((payment) => payment.id !== paymentId);
                setPayments(updatedPayments);
                setSelectedPayment(null);
            }).catch( function(result){
            console.error('Error deleting payment:', error);
            setError(error.message || 'Failed to delete payment');
        });
    };

    const handleDeleteMemberClick = async () => {
        const token = localStorage.getItem('jwt');
        const paymentId = selectedPayment.id;

        var params = {
            id: paymentId,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiPaymentMemberDeleteIdDelete(params, body, additionalParams)
            .then(function(result){
                alert('payment deleted successfully');
                const updatedPayments = payments.filter((payment) => payment.id !== paymentId);
                setPayments(updatedPayments);
                setSelectedPayment(null);
            }).catch( function(result){
            //This is where you would put an error callback
        });
    };



    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
         if (displayContent.paymentsList) {
            setDisplayContent({
                selection: true,
                welcome: false,
                paymentsList: false,
            });
        } else if (displayContent.welcome) {
            setDisplayContent({
                selection: true,
                welcome: false,
                paymentsList: false,
            });
        } else if (displayContent.memberSearch) {
             setDisplayContent({
                 selection: true,
                 welcome: false,
                 paymentsList: false,
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
                        <button onClick={() => handlePaymentTypeSelection('CLIENTS')}>CLIENTS</button>
                        <button onClick={() => handlePaymentTypeSelection('MEMBERS')}>MEMBERS</button>
                    </>
                )}
                {displayContent.welcome && (
                    <>
                        <h1>Clients Payments Manager</h1>
                        <button onClick={handleSearchClick}>SEARCH</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.memberSearch && (
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
                {displayContent.paymentsList && payments.length > 0 && (
                    <div className="payments-list">
                        <h2>{paymentType === 'CLIENTS' ? 'Clients Payments List' : 'Members Payments List'}</h2>
                        <ul>
                            {payments.map((payment) => (
                                <li
                                    key={payment.id}
                                    className={`payments-item ${selectedPayment && selectedPayment.id === payment.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(payment)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedPayment && selectedPayment.id === payment.id}
                                        onChange={() => handleCheckboxChange(payment)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {payment.id}</p>
                                        <p><strong>Causal:</strong> {payment.causal}</p>
                                        <p><strong>Emission Date:</strong> {payment.emissionDate}</p>
                                        <p><strong>Amount:</strong> {payment.amount}</p>
                                        <p><strong>Email:</strong> {paymentType === 'CLIENTS' ? payment.clientEmail : payment.memberEmail}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="payments-actions">
                            {paymentType === 'CLIENTS' ? (
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

export default PaymentsManager;
