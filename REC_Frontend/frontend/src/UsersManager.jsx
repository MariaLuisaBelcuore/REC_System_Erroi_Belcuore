import React, { useEffect, useState } from 'react';
import './CSS/UsersManager.css';

const UsersManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [error, setError] = useState('');
    const [userType, setUserType] = useState(null);

    const [userData, setUserData] = useState({
        nome: '',
        cognome: '',
        email: '',
        password: '',
        ruolo: '',
    });

    const [displayContent, setDisplayContent] = useState({
        selection: true,
        welcome: false,
        usersList: false,
        update: false,
    });

    useEffect(() => {
        setDisplayContent({
            selection: true,
            welcome: false,
            usersList: false,
            update: false,
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
        let url = '';


        var params = {
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        if (userType === 'CLIENTS') {

            apigClient.apiManagementSearchAllClientGet(params, body, additionalParams)
                .then(function(result){
                    setUsers(result.data.list);
                    setSelectedUser(null);
                    setDisplayContent({
                        selection: false,
                        welcome: false,
                        usersList: true,
                        update: false,
                    });
                }).catch( function(result){
                console.error('Error fetching users:', error);
                setError(error.message || 'Failed to fetch users');
            });
        } else if (userType === 'MEMBERS') {

            apigClient.apiManagementSearchAllMemberGet(params, body, additionalParams)
                .then(function(result){
                    setUsers(result.data.list);
                    setSelectedUser(null);
                    setDisplayContent({
                        selection: false,
                        welcome: false,
                        usersList: true,
                        update: false,
                    });
                }).catch( function(result){
                console.error('Error fetching users:', error);
                setError(error.message || 'Failed to fetch users');
            });
        }
    };

    const handleUserTypeSelection = (type) => {
        setUserType(type);
        setDisplayContent({
            selection: false,
            welcome: true,
            usersList: false,
            update: false,
        });
    };

    const handleCheckboxChange = (user) => {
        setSelectedUser(user);
    };

    const handleDeleteClick = async () => {
        const token = localStorage.getItem('jwt');
        const userEmail = selectedUser.email;


        var params = {
            email: userEmail,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiManagementDeleteForAdminEmailDelete(params, body, additionalParams)
            .then(function(result){
                alert('User deleted successfully');
                const updatedUsers = users.filter((user) => user.email !== userEmail);
                setUsers(updatedUsers);
                setSelectedUser(null);
            }).catch( function(result){
            console.error('Error deleting user:', error);
            setError(error.message || 'Failed to delete user');
        });
    };

    const handleUpdateClick = () => {
        setDisplayContent({
            selection: false,
            welcome: false,
            usersList: false,
            update: true,
        });
    };

    const handleApplyClick = async () => {
        const token = localStorage.getItem('jwt');
        const userId = selectedUser.id;
        userData.email = selectedUser.email;
        userData.ruolo = selectedUser.ruolo;

        var params = {
            id: userId,
            email: userData.email,
            Authorization: `Bearer ${token}`
        };
        var body = {...userData};
        var additionalParams = {};

        apigClient.apiManagementUpdateForAdminIdPatch(params, body, additionalParams)
            .then(function(result){
                alert('User updated successfully');
                setDisplayContent({
                    selection: false,
                    welcome: false,
                    usersList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error updating user:', error);
            setError(error.message || 'Failed to update user');
        });
    };

    const handleUserInputChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleBackClick = () => {
        if (displayContent.update) {
            setDisplayContent({
                selection: false,
                welcome: false,
                usersList: true,
                update: false,
            });
        } else if (displayContent.usersList) {
            setDisplayContent({
                selection: true,
                welcome: false,
                usersList: false,
                update: false,
            });
        } else if (displayContent.welcome) {
            setDisplayContent({
                selection: true,
                welcome: false,
                usersList: false,
                update: false,
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
                        <h1>Users Manager</h1>
                        <button onClick={() => handleUserTypeSelection('CLIENTS')}>CLIENTS</button>
                        <button onClick={() => handleUserTypeSelection('MEMBERS')}>MEMBERS</button>
                    </>
                )}
                {displayContent.welcome && (
                    <>
                        <h1>Users Manager</h1>
                        <button onClick={handleSearchClick}>SEARCH</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.usersList && users.length > 0 && (
                    <div className="users-list">
                        <h2>Users List</h2>
                        <ul>
                            {users.map((user) => (
                                <li
                                    key={user.id}
                                    className={`users-item ${selectedUser && selectedUser.id === user.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(user)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedUser && selectedUser.id === user.id}
                                        onChange={() => handleCheckboxChange(user)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {user.id}</p>
                                        <p><strong>Nome:</strong> {user.nome}</p>
                                        <p><strong>Cognome:</strong> {user.cognome}</p>
                                        <p><strong>Email:</strong> {user.email}</p>
                                        <p><strong>Ruolo:</strong> {user.ruolo}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="users-actions">
                            <button onClick={handleDeleteClick} className="action-button">DELETE</button>
                            <button onClick={handleUpdateClick} className="action-button">UPDATE</button>
                        </div>
                    </div>
                )}
                {displayContent.update && (
                    <>
                        <h2>Update User</h2>
                        <label>Nome:</label>
                        <input
                            type="text"
                            name="nome"
                            value={userData.nome}
                            onChange={handleUserInputChange}
                            required
                        /><br />
                        <label>Cognome:</label>
                        <input
                            type="text"
                            name="cognome"
                            value={userData.cognome}
                            onChange={handleUserInputChange}
                            required
                        /><br />
                        <label>Password:</label>
                        <input
                            type="password"
                            name="password"
                            value={userData.password}
                            onChange={handleUserInputChange}
                            required
                        /><br />
                        <button onClick={handleApplyClick}>Apply</button>
                    </>
                )}
            </main>
        </div>
    );
};

export default UsersManager;
