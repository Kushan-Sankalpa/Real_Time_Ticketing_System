import React, { useState } from 'react';
import ConfigurationForm from './Components/ConfigurationForm';
import ControlPanel from './Components/ControlPanel';
import { Container, Alert } from 'react-bootstrap';

const App = () => {
  const [configuration, setConfiguration] = useState(null);
  const [showAlert, setShowAlert] = useState(false);


  // Handler to save the configuration and display an alert
  const handleSaveConfiguration = (config) => {
    setConfiguration(config);
    setShowAlert(true);
  };


  // Handler to reset the configuration and hide the alert
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
