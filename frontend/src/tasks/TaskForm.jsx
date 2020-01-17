import React, { useState } from 'react'
import { Col, Row, Button } from 'react-bootstrap'

const InputField = ({ name, type, label, ...props}) => (
  <div className="form-group">
    <label htmlFor={name}>{label}</label>
    <input type={type} name={name} className="form-control" {...props} />
  </div>
)

const SelectField = ({ name, label, values, ...props}) => (
  <div className="form-group">
    <label htmlFor={name}>{label}</label>
    <select name={name} className="form-control" {...props}> 
      {Object.entries(values).map(([key, value]) => {
        return (
          <option value={key} key={key}>
            {value}
          </option >
        )
      })}
    </select >
  </div>
)

const TaskInputField = ({ field, formValues, setFormValues }) => {
  switch (field.type.name) {
    case 'string':
      return (
        <InputField
          type={field.id === 'password' ? 'password' : 'text'}
          name={field.id}
          label={field.label}
          value={formValues[field.id] || ''}
          onChange={e => {
            setFormValues({
              ...formValues,
              [field.id]: e.target.value
            })
          }} />
      )
    case 'boolean':
      return (
        <InputField
          type="checkbox"
          name={field.id}
          label={field.label}
          value={formValues[field.id] || false}
          onChange={e => {
            setFormValues({
              ...formValues,
              [field.id]: e.target.checked
            })
          }} />
      )
    case 'long':
      return (
        <InputField
          type="number"
          name={field.id}
          label={field.label}
          value={formValues[field.id] || 0}
          onChange={e => {
            setFormValues({
              ...formValues,
              [field.id]: e.target.value
            })
          }} />
      )
    case 'enum':
      return (
        <SelectField
          name={field.id}
          label={field.label}
          values={field.type.values}
          onChange={e => {
            setFormValues({
              ...formValues,
              [field.id]: e.target.value
            })
          }} />
      )
    default:
      break;
    }
}

export const TaskForm = ({ task, onSubmit }) => {
  const { formFields } = task

  const [formValues, setFormValues] = useState({});

  const prepareFormValues = values => {
    return Object.entries(values).map(([key, value]) => {
      return {
        fieldId: key,
        fieldValue: value
      }
    })
  }

  const onSave = async e => {
    e.preventDefault()
    onSubmit(prepareFormValues(formValues))
    setFormValues({})
  }

  return (
    <>
      <Row className="justify-content-md-center">
        <Col md md={3}>
          <h4>{task.name}</h4>
        </Col>
      </Row>
      <form onSubmit={onSave}>
        {formFields && formFields.map(field => {
          return (
            <Row className="justify-content-md-center" key={field.id}>
              <Col md={3}>
                <TaskInputField field={field} formValues={formValues} setFormValues={setFormValues} />
              </Col>
            </Row>
          )
        })}
        <Row className="justify-content-md-center">
          <Col md={3}>
            <div style={{ float: 'right' }}>
              <Button type="submit">Submit</Button>
              <Button variant="secondary" type="reset" onClick={() => setFormValues({})}>Cancel</Button>
            </div>
          </Col>
        </Row>
      </form>
    </>
  )
}