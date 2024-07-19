import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './CSS/Registration.css';

const Registration = () => {
    const [apigClient, setApigClient] = useState(null);
    const navigate = useNavigate();
    const [role, setRole] = useState('');
    const [registrationError, setRegistrationError] = useState(null);
    const [formData, setFormData] = useState({
        nome: '',
        cognome: '',
        email: '',
        password: '',
        ruolo: '',
        adminCode: '',
    });


    useEffect (() => {
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };


    const handleRoleChange = (event) => {
        const selectedRole = event.target.value;
        setRole(selectedRole);
        setFormData({ ...formData, ruolo: selectedRole });
    };


    const handleRegistrationClick = async (e) => {
        e.preventDefault();

        let apiUrl = '';
        let payload = { ...formData };

        switch (formData.ruolo) {
            case 'CLIENT':
                var params = {};
                var body = {...payload};
                var additionalParams = {};

                apigClient.apiRegistrationClientPost(params, body, additionalParams)
                    .then(function(result){
                        alert('Registration successful!');
                        setTimeout(() => {
                            navigate('/'); // Reindirizza verso la pagina di login dopo 3 secondi
                        }, 3000);
                    }).catch( function(result){
                    console.error('Error during registration:', registrationError);
                    setRegistrationError('Registration failed');
                });
                break;
            case 'MEMBER':
                var params = {};
                var body = {...payload};
                var additionalParams = {};

                apigClient.apiRegistrationMemberPost(params, body, additionalParams)
                    .then(function(result){
                        alert('Registration successful!');
                        setTimeout(() => {
                            navigate('/'); // Reindirizza verso la pagina di login dopo 3 secondi
                        }, 3000);
                    }).catch( function(result){
                    console.error('Error during registration:', registrationError);
                    setRegistrationError('Registration failed');
                });
                break;
            case 'ADMIN':
                var params = {
                    code: formData.adminCode
                };
                var body = {...payload};
                var additionalParams = {};

                apigClient.apiRegistrationAdminPost(params, body, additionalParams)
                    .then(function(result){
                        alert('Registration successful!');
                        setTimeout(() => {
                            navigate('/'); // Reindirizza verso la pagina di login dopo 3 secondi
                        }, 3000);
                    }).catch( function(result){
                    console.error('Error during registration:', registrationError);
                    setRegistrationError('Registration failed');
                });
                break;
            default:
                console.error('Invalid role');
                return;
        }
    };

    return (
        <div className="registration">
            <div className="background-registration"></div>
            <div className="registration-form">
                <input type="text" name="nome" placeholder="Nome" className="input-field" value={formData.nome} onChange={handleChange} required />
                <input type="text" name="cognome" placeholder="Cognome" className="input-field" value={formData.cognome} onChange={handleChange} required />
                <input type="email" name="email" placeholder="Email" className="input-field" value={formData.email} onChange={handleChange} required />
                <input type="password" name="password" placeholder="Password" className="input-field" value={formData.password} onChange={handleChange} required />
                {role === 'ADMIN' && (
                    <input type="text" name="adminCode" placeholder="Code" className="input-field" value={formData.adminCode} onChange={handleChange} required />
                )}
                <select name="ruolo" className="input-field" value={role} onChange={handleRoleChange} required>
                    <option value="">Select Role</option>
                    <option value="CLIENT">CLIENT</option>
                    <option value="ADMIN">ADMIN</option>
                    <option value="MEMBER">MEMBER</option>
                </select>
                <button onClick={handleRegistrationClick}>Sign In</button>
                {registrationError && <p style={{ color: 'red' }}>{registrationError}</p>}
            </div>
        </div>
    );
};

export default Registration;
