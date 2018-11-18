import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './credit-proposal.reducer';
import { ICreditProposal } from 'app/shared/model/credit-proposal.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICreditProposalDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CreditProposalDetail extends React.Component<ICreditProposalDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { creditProposalEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="calcardApp.creditProposal.detail.title">CreditProposal</Translate> [<b>{creditProposalEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="clientName">
                <Translate contentKey="calcardApp.creditProposal.clientName">Client Name</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.clientName}</dd>
            <dt>
              <span id="taxpayerId">
                <Translate contentKey="calcardApp.creditProposal.taxpayerId">Taxpayer Id</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.taxpayerId}</dd>
            <dt>
              <span id="clientAge">
                <Translate contentKey="calcardApp.creditProposal.clientAge">Client Age</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.clientAge}</dd>
            <dt>
              <span id="gender">
                <Translate contentKey="calcardApp.creditProposal.gender">Gender</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.gender}</dd>
            <dt>
              <span id="maritalStatus">
                <Translate contentKey="calcardApp.creditProposal.maritalStatus">Marital Status</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.maritalStatus}</dd>
            <dt>
              <span id="federationUnit">
                <Translate contentKey="calcardApp.creditProposal.federationUnit">Federation Unit</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.federationUnit}</dd>
            <dt>
              <span id="dependents">
                <Translate contentKey="calcardApp.creditProposal.dependents">Dependents</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.dependents}</dd>
            <dt>
              <span id="income">
                <Translate contentKey="calcardApp.creditProposal.income">Income</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.income}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="calcardApp.creditProposal.status">Status</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.status}</dd>
            <dt>
              <span id="rejectionReason">
                <Translate contentKey="calcardApp.creditProposal.rejectionReason">Rejection Reason</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.rejectionReason}</dd>
            <dt>
              <span id="aprovedMin">
                <Translate contentKey="calcardApp.creditProposal.aprovedMin">Aproved Min</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.aprovedMin}</dd>
            <dt>
              <span id="aprovedMax">
                <Translate contentKey="calcardApp.creditProposal.aprovedMax">Aproved Max</Translate>
              </span>
            </dt>
            <dd>{creditProposalEntity.aprovedMax}</dd>
            <dt>
              <span id="creationDate">
                <Translate contentKey="calcardApp.creditProposal.creationDate">Creation Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={creditProposalEntity.creationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="processingDate">
                <Translate contentKey="calcardApp.creditProposal.processingDate">Processing Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={creditProposalEntity.processingDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/credit-proposal" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/credit-proposal/${creditProposalEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ creditProposal }: IRootState) => ({
  creditProposalEntity: creditProposal.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CreditProposalDetail);
