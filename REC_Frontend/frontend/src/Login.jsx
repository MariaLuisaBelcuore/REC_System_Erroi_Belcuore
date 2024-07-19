import React, { useState, useEffect } from 'react';
import { FaFacebook, FaTwitter, FaInstagram } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './CSS/Login.css';

const Login = () => {
    const [apigClient, setApigClient] = useState(null);
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');



    useEffect (() => {
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);



    const handleRegistrationClick = () => {
        navigate('/registration');
    };

    const handleLogin = async () => {


        var params = {};
        var body = {
            email: email,
            password: password
        };
        var additionalParams = {};

        apigClient.apiLoginPost(params, body, additionalParams)
            .then(function(result){
                const jwt = result.data.jwt
                const decodedJwt = jwtDecode(jwt);

                // Logica per reindirizzare in base al ruolo
                if (decodedJwt.role === 'CLIENT') {
                    localStorage.setItem('email', email);
                    localStorage.setItem('jwt', jwt);
                    navigate('/homepageclient');
                } else if (decodedJwt.role === 'MEMBER') {
                    localStorage.setItem('email', email);
                    localStorage.setItem('jwt', jwt);
                    navigate('/homepagemember');
                } else if (decodedJwt.role === 'ADMIN') {
                    localStorage.setItem('email', email);
                    localStorage.setItem('jwt', jwt);
                    navigate('/homepageadmin');
                }else {
                    throw new Error('Invalid role');
                }
            }).catch( function(result){
            console.error('Login error:', error);
            setError(error.message || 'Login failed');
        });

    };

    return (
        <div className="login">
            <div className="background-login"></div>
            <div className="logo-container">
                <img src={`${process.env.PUBLIC_URL}/Logo.png`} alt="Logo del sito" />
            </div>
            <div className="login-form">
                <input
                    type="email"
                    placeholder="Email"
                    className="input-field animated-input"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    className="input-field animated-input"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button onClick={handleLogin}>Login</button>
                <button onClick={handleRegistrationClick}>Registration</button>
                {error && <p className="error-message">{error}</p>}
            </div>
            <div className="social-icons">
                <a href="https://www.facebook.com/" className="social-icon">
                    <FaFacebook size={40} />
                </a>
                <a href="https://www.twitter.com/" className="social-icon">
                    <FaTwitter size={40} />
                </a>
                <a href="https://www.instagram.com/" className="social-icon">
                    <FaInstagram size={40} />
                </a>
            </div>
        </div>
    );
};

export default Login;

