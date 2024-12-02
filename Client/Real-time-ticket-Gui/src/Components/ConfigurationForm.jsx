
import React, { useState } from 'react';
import apiClient from '../axios';
import { Form, Button, Alert } from 'react-bootstrap';

const ConfigurationForm = ({ onSave }) => {
  // State variables for form data, errors, and submission error
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
  const [submitError, setSubmitError] = useState('');


  // Function to validate the form inputs
  const validateForm = () => {
    const newErrors = {};
    const {
      ticketReleaseRate,
      customerRetrievalRate,
      maxTicketCapacity,
      totalTickets,
      initialTickets,
      numberOfCustomers,
      numberOfVendors,
    } = formData;

    // Validation logic for each field
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
      initialTickets < 0 ||
      parseInt(initialTickets) > parseInt(totalTickets)
    ) {
      newErrors.initialTickets =
        'Initial tickets must be zero or greater and less than or equal to total tickets.';
    }
    if (!numberOfVendors || numberOfVendors <= 0) {
      newErrors.numberOfVendors = 'Number of vendors must be greater than 0.';
    }
    if (!numberOfCustomers || numberOfCustomers <= 0) {
      newErrors.numberOfCustomers = 'Number of customers must be greater than 0.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handler for input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };


  // Handler for form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitError('');
    if (validateForm()) {
      try {
        const response = await apiClient.post('/configurations/saveConfigurations', formData);
        alert('Configuration saved successfully!');
        onSave(response.data);
      } catch (error) {
        console.error('Error saving configuration:', error);
        if (error.response && error.response.data && error.response.data.error) {
          setSubmitError(error.response.data.error);
        } else {
          setSubmitError('Failed to save configuration. Please try again.');
        }
      }
    }
  };
   // Handler to clear the form fields
  const handleDeleteConfiguration = () => {
    if (window.confirm('Are you sure you want to clear the form?')) {
      setFormData({
        ticketReleaseRate: '',
        customerRetrievalRate: '',
        maxTicketCapacity: '',
        totalTickets: '',
        initialTickets: '',
        numberOfCustomers: '',
        numberOfVendors: '',
      });
      setErrors({});
      setSubmitError('');
    }
  };

  return (
    <Form onSubmit={handleSubmit}>
      <h2 className="text-center mb-4">Configuration Form</h2>

      <Form.Group className="mb-3">
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

      <Form.Group className="mb-3">
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

      <Form.Group className="mb-3">
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

      <Form.Group className="mb-3">
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

      <Form.Group className="mb-3">
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

      <Form.Group className="mb-3">
        <Form.Label>Number of Customers</Form.Label>
        <Form.Control
          type="number"
          name="numberOfCustomers"
          value={formData.numberOfCustomers}
          onChange={handleChange}
          isInvalid={!!errors.numberOfCustomers}
        />
        <Form.Control.Feedback type="invalid">
          {errors.numberOfCustomers}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label>Number of Vendors</Form.Label>
        <Form.Control
          type="number"
          name="numberOfVendors"
          value={formData.numberOfVendors}
          onChange={handleChange}
          isInvalid={!!errors.numberOfVendors}
        />
        <Form.Control.Feedback type="invalid">
          {errors.numberOfVendors}
        </Form.Control.Feedback>
      </Form.Group>

      {submitError && <Alert variant="danger">{submitError}</Alert>}

      <div className="text-center">
        <Button variant="primary" type="submit" className="me-2">
          Save Configuration
        </Button>
        <Button variant="danger" onClick={handleDeleteConfiguration}>
          Clear Configuration
        </Button>
      </div>
    </Form>
  );
};

export default ConfigurationForm;
