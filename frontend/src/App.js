import React, { useEffect } from 'react'
import shallow from 'zustand/shallow'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import { Navbar, Nav, Button } from 'react-bootstrap'

import history from './history';
import { useAuthStore } from './auth/state'
import { RegistrationProcess } from './registration'
import { AssignedTasksList } from './tasks/AssignedTasksList'
import { LoginForm } from './auth'
import { TaskPage } from './tasks/TaskPage'
import './App.css'

function App() {
  const { user, actions } = useAuthStore(
    store => ({
      user: store.user,
      actions: store.actions
    }),
    shallow
  )

  return (
    <Router history={history}>
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="/">Home placeholder</Navbar.Brand>
        {user && (
          <Nav className="ml-auto">
            <Button onClick={() => actions.logout()}>Logout</Button>
          </Nav>
        )}
      </Navbar>
      <Switch>
          <Route path="/register">
            {!user && (
              <RegistrationProcess />
            )}
          </Route>
          <Route path="/signin">
            {!user && (
              <LoginForm />
            )}
          </Route>
          <Route path="/task/:taskId">
            <TaskPage />
          </Route>
          <Route path="/">
            <AssignedTasksList />
          </Route>
        </Switch>
    </Router>
  );
}

export default App;
