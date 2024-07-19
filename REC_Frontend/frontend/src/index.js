import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './CSS/index.css';
import Login from './Login';
import Registration from './Registration';
import reportWebVitals from './reportWebVitals';
import HomepageClient from "./HomepageClient";
import HomepageMember from "./HomepageMember";
import HomepageAdmin from "./HomepageAdmin";
import TasksManager from "./TasksManager";
import EmailsManager from "./EmailsManager";
import EnergyResourcesManager from "./EnergyResourcesManager";
import RewardsManager from "./RewardsManager";
import UsersManager from "./UsersManager";
import PaymentsManager from "./PaymentsManager";
import WalletsManager from "./WalletsManager";


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <Router>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/registration" element={<Registration />} />
                <Route path="/homepageclient" element={<HomepageClient />} />
                <Route path="/homepagemember" element={<HomepageMember />} />
                <Route path="/homepageadmin" element={<HomepageAdmin />} />
                <Route path="/tasks" element={<TasksManager />} />
                <Route path="/emails" element={<EmailsManager />} />
                <Route path="/energy-resources" element={<EnergyResourcesManager />} />
                <Route path="/wallets" element={<WalletsManager />} />
                <Route path="/payments" element={<PaymentsManager />} />
                <Route path="/users" element={<UsersManager />} />
                <Route path="/rewards" element={<RewardsManager />} />
            </Routes>
        </Router>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
