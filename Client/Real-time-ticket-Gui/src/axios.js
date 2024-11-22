// File: src/axios.js

import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // Ensure this matches your backend URL
  headers: {
    'Content-Type': 'application/json',
  },
});

export default apiClient;