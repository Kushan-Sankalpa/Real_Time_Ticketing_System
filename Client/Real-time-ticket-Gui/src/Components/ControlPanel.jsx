// File: src/components/ControlPanel.jsx

import React from 'react';
import apiClient from '../axios';
import { Button, ButtonGroup, Card, Table, Modal, Row, Col } from 'react-bootstrap';
import TicketStatistics from './TicketStatistics';
import TicketStatus from './TicketStatus';

const ControlPanel = ({ configuration, onReset }) => {
  const [showModal, setShowModal] = React.useState(false);
  const [modalMessage, setModalMessage] = React.useState('');
  const [statistics, setStatistics] = React.useState(null);

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

  const handleReset = async () => {
    if (
      window.confirm(
        'Are you sure you want to reset the configuration? This will delete all tickets and the latest configuration.'
      )
    ) {
      try {
        const response = await apiClient.delete('/system/reset');
        alert(response.data);
        onReset();
      } catch (error) {
        console.error('Error resetting the system:', error);
        alert('Failed to reset the system. Please try again.');
      }
    }
  };

  const handleShowStatistics = async () => {
    try {
      const response = await apiClient.get('/tickets/statistics');
      setStatistics(response.data);
      setShowModal(true);
    } catch (error) {
      console.error('Error fetching ticket statistics:', error);
      setModalMessage('Failed to fetch ticket statistics. Please try again.');
      setShowModal(true);
    }
  };

  return (
    <div>
      <h2 className="text-center mb-4">Control Panel</h2>
      {configuration && (
        <Row>
          <Col md={6} className="mb-4">
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

            <div className="text-center">
              <ButtonGroup className="mb-3">
                <Button variant="success" onClick={handleStart} className="me-2">
                  Start
                </Button>
                <Button variant="danger" onClick={handleStop} className="me-2">
                  Stop
                </Button>
                <Button variant="secondary" onClick={handleReset}>
                  Reset
                </Button>
              </ButtonGroup>
            </div>
          </Col>

          <Col md={6}>
            <TicketStatus />
            <div className="text-center mt-3">
              <Button variant="info" onClick={handleShowStatistics}>
                Ticket Statistics
              </Button>
            </div>
          </Col>
        </Row>
      )}

      <Modal
        show={showModal}
        onHide={() => {
          setShowModal(false);
          setStatistics(null);
          setModalMessage('');
        }}
      >
        <Modal.Header closeButton>
          <Modal.Title>{statistics ? 'Ticket Statistics' : 'System Status'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {statistics ? (
            <TicketStatistics statistics={statistics} />
          ) : (
            <p>{modalMessage}</p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="primary"
            onClick={() => {
              setShowModal(false);
              setStatistics(null);
              setModalMessage('');
            }}
          >
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ControlPanel;
