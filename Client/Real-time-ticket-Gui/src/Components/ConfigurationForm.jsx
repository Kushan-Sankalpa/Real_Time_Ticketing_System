// src/components/ConfigurationForm.jsx
import React, { useState } from 'react';
import apiClient from '../axios';
import { Form, Button, Alert } from 'react-bootstrap';

const ConfigurationForm = ({ onSave }) => {
  const [formData, setFormData] = useState({
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: '',
    totalTickets: '',
    initialTickets: '',
    numberOfCustomers: '',
    numberOfVendors: '',
  });

  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};
    const {
      ticketReleaseRate,
      customerRetrievalRate,
      maxTicketCapacity,
      totalTickets,
      initialTickets,
    } = formData;

    if (!ticketReleaseRate || ticketReleaseRate <= 0) {
      newErrors.ticketReleaseRate = 'Ticket release rate must be greater than 0.';
    }
    if (!customerRetrievalRate || customerRetrievalRate <= 0) {
      newErrors.customerRetrievalRate = 'Customer retrieval rate must be greater than 0.';
    }
    if (!maxTicketCapacity || maxTicketCapacity <= 0) {
      newErrors.maxTicketCapacity = 'Maximum ticket capacity must be greater than 0.';
    }
    if (
      !totalTickets ||
      totalTickets <= 0 ||
      parseInt(totalTickets) > parseInt(maxTicketCapacity)
    ) {
      newErrors.totalTickets =
        'Total tickets must be greater than 0 and less than or equal to maximum ticket capacity.';
    }
    if (
      !initialTickets ||
      initialTickets <= 0 ||
      parseInt(initialTickets) > parseInt(totalTickets)
    ) {
      newErrors.initialTickets =
        'Initial tickets must be greater than 0 and less than or equal to total tickets.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      try {
        const response = await apiClient.post('/configurations/saveConfigurations', formData);
        alert('Configuration saved successfully!');
        onSave(response.data);
      } catch (error) {
        alert('Failed to save configuration. Please try again.');
      }
    }
  };

  return (
    <Form onSubmit={handleSubmit}>
      <h2>Configuration Form</h2>

      {/* Ticket Release Rate */}
      <Form.Group>
        <Form.Label>Ticket Release Rate (ms)</Form.Label>
        <Form.Control
          type="number"
          name="ticketReleaseRate"
          value={formData.ticketReleaseRate}
          onChange={handleChange}
          isInvalid={!!errors.ticketReleaseRate}
        />
        <Form.Control.Feedback type="invalid">
          {errors.ticketReleaseRate}
        </Form.Control.Feedback>
      </Form.Group>

      {/* Customer Retrieval Rate */}
      <Form.Group>
        <Form.Label>Customer Retrieval Rate (ms)</Form.Label>
        <Form.Control
          type="number"
          name="customerRetrievalRate"
          value={formData.customerRetrievalRate}
          onChange={handleChange}
          isInvalid={!!errors.customerRetrievalRate}
        />
        <Form.Control.Feedback type="invalid">
          {errors.customerRetrievalRate}
        </Form.Control.Feedback>
      </Form.Group>

      {/* Maximum Ticket Capacity */}
      <Form.Group>
        <Form.Label>Maximum Ticket Capacity</Form.Label>
        <Form.Control
          type="number"
          name="maxTicketCapacity"
          value={formData.maxTicketCapacity}
          onChange={handleChange}
          isInvalid={!!errors.maxTicketCapacity}
        />
        <Form.Control.Feedback type="invalid">
          {errors.maxTicketCapacity}
        </Form.Control.Feedback>
      </Form.Group>

      {/* Total Tickets */}
      <Form.Group>
        <Form.Label>Total Tickets</Form.Label>
        <Form.Control
          type="number"
          name="totalTickets"
          value={formData.totalTickets}
          onChange={handleChange}
          isInvalid={!!errors.totalTickets}
        />
        <Form.Control.Feedback type="invalid">
          {errors.totalTickets}
        </Form.Control.Feedback>
      </Form.Group>

      {/* Initial Tickets */}
      <Form.Group>
        <Form.Label>Initial Tickets</Form.Label>
        <Form.Control
          type="number"
          name="initialTickets"
          value={formData.initialTickets}
          onChange={handleChange}
          isInvalid={!!errors.initialTickets}
        />
        <Form.Control.Feedback type="invalid">
          {errors.initialTickets}
        </Form.Control.Feedback>
      </Form.Group>

      {/* Number of Customers */}
      <Form.Group>
        <Form.Label>Number of Customers</Form.Label>
        <Form.Control
          type="number"
          name="numberOfCustomers"
          value={formData.numberOfCustomers}
          onChange={handleChange}
        />
      </Form.Group>

      {/* Number of Vendors */}
      <Form.Group>
        <Form.Label>Number of Vendors</Form.Label>
        <Form.Control
          type="number"
          name="numberOfVendors"
          value={formData.numberOfVendors}
          onChange={handleChange}
        />
      </Form.Group>

      <Button variant="primary" type="submit" className="mt-3">
        Save Configuration
      </Button>
    </Form>
  );
};

export default ConfigurationForm;
