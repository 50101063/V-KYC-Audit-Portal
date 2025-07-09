// Frontend Environment Variables Template
// This file provides a template for configuring frontend environment variables.
// In a real React project, you would typically use a .env file (e.g., .env.development, .env.production)
// or a build-time configuration for this.

// Example for a .env file (e.g., .env.development):
// REACT_APP_BACKEND_API_BASE_URL=http://localhost:8080/api/v1

// Example for a JavaScript configuration object (if not using .env files directly)
const config = {
  // Base URL for the backend API.
  // In development, this might be your local backend server.
  // In production, this would be the URL of your deployed backend API Gateway/Load Balancer.
  backendApiBaseUrl: process.env.REACT_APP_BACKEND_API_BASE_URL || 'http://localhost:8080/api/v1',

  // Add any other frontend-specific environment variables here.
  // For example:
  // googleAnalyticsId: process.env.REACT_APP_GA_ID,
};

export default config;
