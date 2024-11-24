// File: src/App.jsx

import React, { useState } from 'react';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import { Container, Alert } from 'react-bootstrap';

const App = () => {
  const [configuration, setConfiguration] = useState(null);
  const [showAlert, setShowAlert] = useState(false);

  const handleSaveConfiguration = (config) => {
    setConfiguration(config);
    setShowAlert(true);
  };

  const handleResetConfiguration = () => {
    setConfiguration(null);
    setShowAlert(false);
  };

  return (
    <Container className="mt-4">
      <h1 className="mb-4 text-center">Real-Time Ticketing System</h1>
      {showAlert && (
        <Alert variant="info" onClose={() => setShowAlert(false)} dismissible>
          Configuration has been saved. You can now start the system.
        </Alert>
      )}
      {!configuration ? (
        <ConfigurationForm onSave={handleSaveConfiguration} />
      ) : (
        <>
          <ControlPanel configuration={configuration} onReset={handleResetConfiguration} />
        </>
      )}
    </Container>
  );
};

export default App;
