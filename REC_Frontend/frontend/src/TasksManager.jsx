import React, { useEffect, useState } from 'react';
import './CSS/TasksManager.css'; // Importa il file CSS per le personalizzazioni

const TasksManager = () => {
    const [apigClient, setApigClient] = useState(null);
    const [clientEmail, setClientEmail] = useState('');
    const [tasks, setTasks] = useState([]);
    const [selectedTask, setSelectedTask] = useState(null);
    const [error, setError] = useState('');

    const [taskData, setTaskData] = useState({
        name: '',
        description: '',
        executionTime: 0,
        linkGit: '',
        os: '',
        memory: 0,
        processorModel: '',
        processorVelocity: 0,
        clientEmail: ''
    });

    const [displayContent, setDisplayContent] = useState({
        welcome: true,
        tasksList: false,
        update: false,
    });

    useEffect(() => {
        setDisplayContent({
            welcome: true,
            tasksList: false,
            update: false,
        });
        if (window.apigClientFactory) {
            const client = window.apigClientFactory.newClient();
            setApigClient(client);
        } else {
            console.error('apigClientFactory is not defined')
        }
    }, []);

    const handleEmailChange = (e) => {
        setClientEmail(e.target.value);
    };

    const handleSearchClick = async () => {
        const token = localStorage.getItem('jwt');


        var params = {
            clientEmail: clientEmail,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiTaskSearchAllGet(params, body, additionalParams)
            .then(function(result){
                setTasks(result.data.list);
                setSelectedTask(null); // Resetta il task selezionato quando vengono caricati nuovi task
                setDisplayContent({
                    welcome: false,
                    tasksList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error fetching tasks:', error);
            setError(error.message || 'Failed to fetch tasks');
        });
    };

    const handleCheckboxChange = (task) => {
        setSelectedTask(task);
    };

    const handleDeleteClick = async () => {
        const token = localStorage.getItem('jwt');
        const taskId = selectedTask.id;



        var params = {
            id: taskId,
            Authorization: `Bearer ${token}`
        };
        var body = {};
        var additionalParams = {};

        apigClient.apiTaskDeleteIdDelete(params, body, additionalParams)
            .then(function(result){
                alert('Task deleted successfully');
                const updatedTasks = tasks.filter((task) => task.id !== taskId);
                setTasks(updatedTasks);
                setSelectedTask(null);
            }).catch( function(result){
            console.error('Error deleting task:', error);
            setError(error.message || 'Failed to delete task');
        });
    };

    const handleUpdateClick = () => {
        setDisplayContent({
            welcome: false,
            tasksList: false,
            update: true, // Mostra il form di aggiornamento
        });
    };

    const handleApplyClick = async () => {
        const token = localStorage.getItem('jwt');
        const taskId = selectedTask.id;
        taskData.clientEmail = selectedTask.clientEmail;


        var params = {
            id: taskId,
            Authorization: `Bearer ${token}`
        };
        var body = {...taskData};
        var additionalParams = {};

        apigClient.apiTaskUpdateIdPatch(params, body, additionalParams)
            .then(function(result){
                alert('Task updated successfully');
                setDisplayContent({
                    welcome: false,
                    tasksList: true,
                    update: false,
                });
            }).catch( function(result){
            console.error('Error updating task:', error);
            setError(error.message || 'Failed to update task');
        });
    };

    const handleTaskInputChange = (e) => {
        const { name, value } = e.target;
        setTaskData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleBackClick = () => {
        // Logica per gestire il click sul pulsante "Back"
        if (displayContent.update) {
            setDisplayContent({
                welcome: false,
                tasksList: true,
                update: false,
            });
        } else if (displayContent.tasksList) {
            setDisplayContent({
                welcome: true,
                tasksList: false,
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
                        <h1>Tasks Manager</h1>
                        <label htmlFor="client-email">Client Email:</label>
                        <input
                            type="email"
                            id="client-email"
                            value={clientEmail}
                            onChange={handleEmailChange}
                        />
                        <button onClick={handleSearchClick}>SEARCH</button>

                        {error && <p className="error-message">{error}</p>}
                    </>
                )}
                {displayContent.tasksList && tasks.length > 0 && (
                    <div className="tasks-list">
                        <h2>Tasks List</h2>
                        <ul>
                            {tasks.map((task) => (
                                <li
                                    key={task.id}
                                    className={`tasks-item ${selectedTask && selectedTask.id === task.id ? 'selected' : ''}`}
                                    onClick={() => handleCheckboxChange(task)}
                                >
                                    <input
                                        type="checkbox"
                                        checked={selectedTask && selectedTask.id === task.id}
                                        onChange={() => handleCheckboxChange(task)}
                                    />
                                    <div>
                                        <p><strong>Id:</strong> {task.id}</p>
                                        <p><strong>Name:</strong> {task.name}</p>
                                        <p><strong>Description:</strong> {task.description}</p>
                                        <p><strong>Execution Time:</strong> {task.executionTime}</p>
                                        <p><strong>Link Git:</strong> <a href={task.linkGit} target="_blank" rel="noopener noreferrer">{task.linkGit}</a></p>
                                        <p><strong>OS:</strong> {task.os}</p>
                                        <p><strong>Memory:</strong> {task.memory}</p>
                                        <p><strong>Processor Model:</strong> {task.processorModel}</p>
                                        <p><strong>Processor Velocity:</strong> {task.processorVelocity} GHz</p>
                                        <p><strong>Client Email:</strong> {task.clientEmail}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="tasks-actions">
                            <button onClick={handleDeleteClick} className="action-button">DELETE</button>
                            <button onClick={handleUpdateClick} className="action-button">UPDATE</button>
                        </div>
                    </div>
                )}
                {displayContent.update && (
                    <>
                        <h2>Update Task</h2>
                        <label>Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={taskData.name}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>Description:</label>
                        <input
                            type="text"
                            name="description"
                            value={taskData.description}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>Execution Time:</label>
                        <input
                            type="number"
                            name="executionTime"
                            value={taskData.executionTime}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>Link Git:</label>
                        <input
                            type="text"
                            name="linkGit"
                            value={taskData.linkGit}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>OS:</label>
                        <input
                            type="text"
                            name="os"
                            value={taskData.os}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>Memory:</label>
                        <input
                            type="number"
                            name="memory"
                            value={taskData.memory}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>Processor Model:</label>
                        <input
                            type="text"
                            name="processorModel"
                            value={taskData.processorModel}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <label>Processor Velocity:</label>
                        <input
                            type="number"
                            name="processorVelocity"
                            value={taskData.processorVelocity}
                            onChange={handleTaskInputChange}
                            required
                        /><br />
                        <button onClick={handleApplyClick}>Apply</button>
                    </>
                )}
            </main>
        </div>
    );
};

export default TasksManager;
