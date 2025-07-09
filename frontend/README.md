# V-KYC Audit Portal Frontend

This project contains the frontend application for the V-KYC Audit Portal, built with React, TypeScript, and MUI.

## Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── App.css
│   ├── App.js
│   └── index.js
└── README.md
```

## Setup and Execution Instructions

To set up and run the frontend application locally, follow these steps:

### Prerequisites

*   Node.js (LTS version, e.g., 18.x or 20.x)
*   npm (Node Package Manager) or Yarn

### 1. Clone the Repository

If you haven't already, clone the entire project repository:

```bash
git clone https://github.com/50101063/V-KYC-Audit-Portal.git
cd V-KYC-Audit-Portal
```

### 2. Navigate to the Frontend Directory

```bash
cd frontend/
```

### 3. Install Dependencies

Install the necessary Node.js packages.

```bash
npm install
# or
# yarn install
```

### 4. Run the Application

Start the development server. This will typically open the application in your default web browser at `http://localhost:3000` (or another available port).

```bash
npm start
# or
# yarn start
```

The application will now be running and accessible in your browser. Any changes you make to the source code will trigger a hot reload.

### 5. Build for Production (Optional)

To create a production-ready build of the application, run:

```bash
npm run build
# or
# yarn build
```

This will create a `build/` directory containing the optimized static assets. These assets can then be deployed to a static web server or integrated with a backend server.

## API Integration

The frontend application will communicate with the V-KYC Backend API. Ensure the backend service is running and accessible. The API endpoints and data formats will be defined and communicated by the Backend Developer.

*   **Base API URL:** This will be configured via environment variables (e.g., `.env` file) during local development and managed by the deployment environment in production.
    *   Example: `REACT_APP_API_BASE_URL=http://localhost:8080/api/v1`

## Collaboration

For seamless integration, please ensure clear communication with the Backend Developer regarding API specifications, data contracts, and error handling.
