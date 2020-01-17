import React, { useState } from 'react'
import shallow from 'zustand/shallow'
import { Col, Row, Button } from 'react-bootstrap'

import history from '../history';
import { useAuthStore } from './state'

export const LoginForm = () => {
    const { actions } = useAuthStore(
      store => ({
        actions: store.actions
      }),
      shallow
    )

    const [formValues, setFormValues] = useState({});

    const onSave = async e => {
      e.preventDefault()
      console.log(formValues)
      await actions.login(formValues)
      setFormValues({})
      history.push('/')
    }
  
    return (
      <>
        <Row className="justify-content-md-center">
          <Col md={3}>
            <h4>Sign in</h4>
          </Col>
        </Row>
        <form onSubmit={onSave}>
          <Row className="justify-content-md-center">
            <Col md={3}>
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <input
                type="text"
                name="username"
                className="form-control"
                value={formValues.username || ''}
                onChange={e => setFormValues({ ...formValues, username: e.target.value })}
              />
            </div>
            </Col>
          </Row>
          <Row className="justify-content-md-center">
            <Col md={3}>
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                name="password"
                className="form-control"
                value={formValues.password || ''}
                onChange={e => setFormValues({ ...formValues, password: e.target.value })}
              />
            </div>
            </Col>
          </Row>
          <Row className="justify-content-md-center">
            <Col md={3}>
              <div style={{ float: 'right' }}>
                <Button type="submit">Sign In</Button>
                <Button variant="secondary" type="reset" onClick={() => setFormValues({})}>Cancel</Button>
              </div>
            </Col>
          </Row>
        </form>
      </>
    )
  }