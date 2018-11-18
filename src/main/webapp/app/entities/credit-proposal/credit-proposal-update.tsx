import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './credit-proposal.reducer';
import { ICreditProposal } from 'app/shared/model/credit-proposal.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICreditProposalUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICreditProposalUpdateState {
  isNew: boolean;
}

export class CreditProposalUpdate extends React.Component<ICreditProposalUpdateProps, ICreditProposalUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { creditProposalEntity } = this.props;
      const entity = {
        ...creditProposalEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/credit-proposal');
  };

  render() {
    const { creditProposalEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="calcardApp.creditProposal.home.createOrEditLabel">
              <Translate contentKey="calcardApp.creditProposal.home.createOrEditLabel">Create or edit a CreditProposal</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : creditProposalEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="credit-proposal-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="clientNameLabel" for="clientName">
                    <Translate contentKey="calcardApp.creditProposal.clientName">Client Name</Translate>
                  </Label>
                  <AvField
                    id="credit-proposal-clientName"
                    type="text"
                    name="clientName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 3, errorMessage: translate('entity.validation.minlength', { min: 3 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="taxpayerIdLabel" for="taxpayerId">
                    <Translate contentKey="calcardApp.creditProposal.taxpayerId">Taxpayer Id</Translate>
                  </Label>
                  <AvField
                    id="credit-proposal-taxpayerId"
                    type="text"
                    name="taxpayerId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="clientAgeLabel" for="clientAge">
                    <Translate contentKey="calcardApp.creditProposal.clientAge">Client Age</Translate>
                  </Label>
                  <AvField
                    id="credit-proposal-clientAge"
                    type="string"
                    className="form-control"
                    name="clientAge"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 18, errorMessage: translate('entity.validation.min', { min: 18 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel">
                    <Translate contentKey="calcardApp.creditProposal.gender">Gender</Translate>
                  </Label>
                  <AvInput
                    id="credit-proposal-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && creditProposalEntity.gender) || 'MALE'}
                  >
                    <option value="MALE">
                      <Translate contentKey="calcardApp.Gender.MALE" />
                    </option>
                    <option value="FEMALE">
                      <Translate contentKey="calcardApp.Gender.FEMALE" />
                    </option>
                    <option value="OTHER">
                      <Translate contentKey="calcardApp.Gender.OTHER" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="maritalStatusLabel">
                    <Translate contentKey="calcardApp.creditProposal.maritalStatus">Marital Status</Translate>
                  </Label>
                  <AvInput
                    id="credit-proposal-maritalStatus"
                    type="select"
                    className="form-control"
                    name="maritalStatus"
                    value={(!isNew && creditProposalEntity.maritalStatus) || 'SINGLE'}
                  >
                    <option value="SINGLE">
                      <Translate contentKey="calcardApp.MaritalStatus.SINGLE" />
                    </option>
                    <option value="MARRIED">
                      <Translate contentKey="calcardApp.MaritalStatus.MARRIED" />
                    </option>
                    <option value="DIVORCED">
                      <Translate contentKey="calcardApp.MaritalStatus.DIVORCED" />
                    </option>
                    <option value="WIDOW">
                      <Translate contentKey="calcardApp.MaritalStatus.WIDOW" />
                    </option>
                    <option value="OTHER">
                      <Translate contentKey="calcardApp.MaritalStatus.OTHER" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="federationUnitLabel">
                    <Translate contentKey="calcardApp.creditProposal.federationUnit">Federation Unit</Translate>
                  </Label>
                  <AvInput
                    id="credit-proposal-federationUnit"
                    type="select"
                    className="form-control"
                    name="federationUnit"
                    value={(!isNew && creditProposalEntity.federationUnit) || 'AC'}
                  >
                    <option value="AC">
                      <Translate contentKey="calcardApp.FederationUnit.AC" />
                    </option>
                    <option value="AL">
                      <Translate contentKey="calcardApp.FederationUnit.AL" />
                    </option>
                    <option value="AP">
                      <Translate contentKey="calcardApp.FederationUnit.AP" />
                    </option>
                    <option value="AM">
                      <Translate contentKey="calcardApp.FederationUnit.AM" />
                    </option>
                    <option value="BA">
                      <Translate contentKey="calcardApp.FederationUnit.BA" />
                    </option>
                    <option value="CE">
                      <Translate contentKey="calcardApp.FederationUnit.CE" />
                    </option>
                    <option value="DF">
                      <Translate contentKey="calcardApp.FederationUnit.DF" />
                    </option>
                    <option value="ES">
                      <Translate contentKey="calcardApp.FederationUnit.ES" />
                    </option>
                    <option value="GO">
                      <Translate contentKey="calcardApp.FederationUnit.GO" />
                    </option>
                    <option value="MA">
                      <Translate contentKey="calcardApp.FederationUnit.MA" />
                    </option>
                    <option value="MT">
                      <Translate contentKey="calcardApp.FederationUnit.MT" />
                    </option>
                    <option value="MS">
                      <Translate contentKey="calcardApp.FederationUnit.MS" />
                    </option>
                    <option value="MG">
                      <Translate contentKey="calcardApp.FederationUnit.MG" />
                    </option>
                    <option value="PA">
                      <Translate contentKey="calcardApp.FederationUnit.PA" />
                    </option>
                    <option value="PB">
                      <Translate contentKey="calcardApp.FederationUnit.PB" />
                    </option>
                    <option value="PR">
                      <Translate contentKey="calcardApp.FederationUnit.PR" />
                    </option>
                    <option value="PE">
                      <Translate contentKey="calcardApp.FederationUnit.PE" />
                    </option>
                    <option value="PI">
                      <Translate contentKey="calcardApp.FederationUnit.PI" />
                    </option>
                    <option value="RJ">
                      <Translate contentKey="calcardApp.FederationUnit.RJ" />
                    </option>
                    <option value="RN">
                      <Translate contentKey="calcardApp.FederationUnit.RN" />
                    </option>
                    <option value="RS">
                      <Translate contentKey="calcardApp.FederationUnit.RS" />
                    </option>
                    <option value="RO">
                      <Translate contentKey="calcardApp.FederationUnit.RO" />
                    </option>
                    <option value="RR">
                      <Translate contentKey="calcardApp.FederationUnit.RR" />
                    </option>
                    <option value="SP">
                      <Translate contentKey="calcardApp.FederationUnit.SP" />
                    </option>
                    <option value="SC">
                      <Translate contentKey="calcardApp.FederationUnit.SC" />
                    </option>
                    <option value="SE">
                      <Translate contentKey="calcardApp.FederationUnit.SE" />
                    </option>
                    <option value="TO">
                      <Translate contentKey="calcardApp.FederationUnit.TO" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="dependentsLabel" for="dependents">
                    <Translate contentKey="calcardApp.creditProposal.dependents">Dependents</Translate>
                  </Label>
                  <AvField
                    id="credit-proposal-dependents"
                    type="string"
                    className="form-control"
                    name="dependents"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="incomeLabel" for="income">
                    <Translate contentKey="calcardApp.creditProposal.income">Income</Translate>
                  </Label>
                  <AvField
                    id="credit-proposal-income"
                    type="text"
                    name="income"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel">
                    <Translate contentKey="calcardApp.creditProposal.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="credit-proposal-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && creditProposalEntity.status) || 'PROCESSING'}
                  >
                    <option value="PROCESSING">
                      <Translate contentKey="calcardApp.CreditProposalStatus.PROCESSING" />
                    </option>
                    <option value="APROVED">
                      <Translate contentKey="calcardApp.CreditProposalStatus.APROVED" />
                    </option>
                    <option value="REJECTED">
                      <Translate contentKey="calcardApp.CreditProposalStatus.REJECTED" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="rejectionReasonLabel">
                    <Translate contentKey="calcardApp.creditProposal.rejectionReason">Rejection Reason</Translate>
                  </Label>
                  <AvInput
                    id="credit-proposal-rejectionReason"
                    type="select"
                    className="form-control"
                    name="rejectionReason"
                    value={(!isNew && creditProposalEntity.rejectionReason) || 'POLICY'}
                  >
                    <option value="POLICY">
                      <Translate contentKey="calcardApp.RejectionReason.POLICY" />
                    </option>
                    <option value="INCOME">
                      <Translate contentKey="calcardApp.RejectionReason.INCOME" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="aprovedMinLabel" for="aprovedMin">
                    <Translate contentKey="calcardApp.creditProposal.aprovedMin">Aproved Min</Translate>
                  </Label>
                  <AvField id="credit-proposal-aprovedMin" type="text" name="aprovedMin" />
                </AvGroup>
                <AvGroup>
                  <Label id="aprovedMaxLabel" for="aprovedMax">
                    <Translate contentKey="calcardApp.creditProposal.aprovedMax">Aproved Max</Translate>
                  </Label>
                  <AvField id="credit-proposal-aprovedMax" type="text" name="aprovedMax" />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    <Translate contentKey="calcardApp.creditProposal.creationDate">Creation Date</Translate>
                  </Label>
                  <AvField id="credit-proposal-creationDate" type="date" className="form-control" name="creationDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="processingDateLabel" for="processingDate">
                    <Translate contentKey="calcardApp.creditProposal.processingDate">Processing Date</Translate>
                  </Label>
                  <AvField id="credit-proposal-processingDate" type="date" className="form-control" name="processingDate" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/credit-proposal" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  creditProposalEntity: storeState.creditProposal.entity,
  loading: storeState.creditProposal.loading,
  updating: storeState.creditProposal.updating,
  updateSuccess: storeState.creditProposal.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CreditProposalUpdate);
