// src/components/ControlPanel.jsx
import React from 'react';
import apiClient from '../axios';
import { Button, ButtonGroup, Card } from 'react-bootstrap';

const ControlPanel = ({ configuration, onReset }) => {
  const handleStart = async () => {
    try {
      const response = await apiClient.get('/system/start');
      alert(response.data);
    } catch (error) {
      alert('Failed to start the system. Please try again.');
    }
  };

  const handleStop = async () => {
    try {
      const response = await apiClient.get('/system/stop');
      alert(response.data);
    } catch (error) {
      alert('Failed to stop the system. Please try again.');
    }
  };

  return (
    <div>
        <h2>Control Panel</h2>
      {configuration && (
        <Card className="mb-3">
          <Card.Header>Saved Configuration</Card.Header>
          <Card.Body>
            <pre>{JSON.stringify(configuration, null, 2)}</pre>
          </Card.Body>
        </Card>
      )}
      <ButtonGroup>
        <Button variant="success" onClick={handleStart}>
          Start
        </Button>
        <Button variant="danger" onClick={handleStop}>
          Stop
        </Button>
        <Button variant="secondary" onClick={onReset}>
          Reset
        </Button>
      </ButtonGroup>
    </div>
    
  );
};

export default ControlPanel;
