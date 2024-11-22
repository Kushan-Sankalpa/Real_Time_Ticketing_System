// File: src/components/ControlPanel.jsx

import React from 'react';
import apiClient from '../axios';
import { Button, ButtonGroup, Card, Table, Modal } from 'react-bootstrap';

const ControlPanel = ({ configuration, onReset }) => {
  const [showModal, setShowModal] = React.useState(false);
  const [modalMessage, setModalMessage] = React.useState('');

  const handleStart = async () => {
    try {
      const response = await apiClient.get('/system/start');
      setModalMessage(response.data);
      setShowModal(true);
    } catch (error) {
      console.error('Error starting the system:', error);
      setModalMessage('Failed to start the system. Please try again.');
      setShowModal(true);
    }
  };

  const handleStop = async () => {
    try {
      const response = await apiClient.get('/system/stop');
      setModalMessage(response.data);
      setShowModal(true);
    } catch (error) {
      console.error('Error stopping the system:', error);
      setModalMessage('Failed to stop the system. Please try again.');
      setShowModal(true);
    }
  };

  // Modify the handleReset function in ControlPanel.jsx

const handleReset = async () => {
  if (window.confirm('Are you sure you want to reset the configuration? This will delete all tickets and the latest configuration.')) {
    try {
      const response = await apiClient.delete('/system/reset');
      alert(response.data);
      onReset(); // Reset the frontend state
    } catch (error) {
      console.error('Error resetting the system:', error);
      alert('Failed to reset the system. Please try again.');
    }
  }
};


  return (
    <div>
      <h2>Control Panel</h2>
      {configuration && (
        <Card className="mb-3">
          <Card.Header>Saved Configuration</Card.Header>
          <Card.Body>
            <Table striped bordered hover>
              <tbody>
                <tr>
                  <th>ID</th>
                  <td>{configuration.id}</td>
                </tr>
                <tr>
                  <th>Ticket Release Rate (ms)</th>
                  <td>{configuration.ticketReleaseRate}</td>
                </tr>
                <tr>
                  <th>Customer Retrieval Rate (ms)</th>
                  <td>{configuration.customerRetrievalRate}</td>
                </tr>
                <tr>
                  <th>Maximum Ticket Capacity</th>
                  <td>{configuration.maxTicketCapacity}</td>
                </tr>
                <tr>
                  <th>Total Tickets</th>
                  <td>{configuration.totalTickets}</td>
                </tr>
                <tr>
                  <th>Initial Tickets</th>
                  <td>{configuration.initialTickets}</td>
                </tr>
                <tr>
                  <th>Number of Vendors</th>
                  <td>{configuration.numberOfVendors}</td>
                </tr>
                <tr>
                  <th>Number of Customers</th>
                  <td>{configuration.numberOfCustomers}</td>
                </tr>
              </tbody>
            </Table>
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
        <Button variant="secondary" onClick={handleReset}>
          Reset
        </Button>
      </ButtonGroup>

      {/* Modal for Start/Stop Messages */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>System Status</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ControlPanel;
