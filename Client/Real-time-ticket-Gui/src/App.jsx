// File: src/App.jsx

import React, { useState } from 'react';
import ConfigurationForm from './Components/ConfigurationForm';
import ControlPanel from './Components/ControlPanel';
import TicketStatus from './Components/TicketStatus';
import { Container } from 'react-bootstrap';

const App = () => {
  const [configuration, setConfiguration] = useState(null);

  const handleSaveConfiguration = (config) => {
    setConfiguration(config);
  };

  const handleResetConfiguration = () => {
    setConfiguration(null);
  };

  return (
    <Container>
      <h1>Real-Time Ticketing System</h1>
      {!configuration ? (
        <ConfigurationForm onSave={handleSaveConfiguration} />
      ) : (
        <>
          <ControlPanel configuration={configuration} onReset={handleResetConfiguration} />
          <TicketStatus />
        </>
      )}
    </Container>
  );
};

export default App;
