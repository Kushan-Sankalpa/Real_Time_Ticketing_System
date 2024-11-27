// File: src/App.jsx

import React, { useState } from 'react';
import ConfigurationForm from './Components/ConfigurationForm';
import ControlPanel from './Components/ControlPanel';
import { Container, Alert } from 'react-bootstrap';

const App = () => {
  const [configuration, setConfiguration] = useState(null);
  const [showAlert, setShowAlert] = useState(false); // Corrected here

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
          {/* Include LogDisplay at the root level if desired */}
          {/* <div className="mt-4">
            <LogDisplay />
          </div> */}
        </>
      )}
    </Container>
  );
};

export default App;
