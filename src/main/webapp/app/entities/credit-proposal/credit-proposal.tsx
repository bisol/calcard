import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './credit-proposal.reducer';
import { ICreditProposal } from 'app/shared/model/credit-proposal.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ICreditProposalProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type ICreditProposalState = IPaginationBaseState;

export class CreditProposal extends React.Component<ICreditProposalProps, ICreditProposalState> {
  state: ICreditProposalState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { creditProposalList, match } = this.props;
    return (
      <div>
        <h2 id="credit-proposal-heading">
          <Translate contentKey="calcardApp.creditProposal.home.title">Credit Proposals</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="calcardApp.creditProposal.home.createLabel">Create new Credit Proposal</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('clientName')}>
                    <Translate contentKey="calcardApp.creditProposal.clientName">Client Name</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('taxpayerId')}>
                    <Translate contentKey="calcardApp.creditProposal.taxpayerId">Taxpayer Id</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('clientAge')}>
                    <Translate contentKey="calcardApp.creditProposal.clientAge">Client Age</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('gender')}>
                    <Translate contentKey="calcardApp.creditProposal.gender">Gender</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('maritalStatus')}>
                    <Translate contentKey="calcardApp.creditProposal.maritalStatus">Marital Status</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('federationUnit')}>
                    <Translate contentKey="calcardApp.creditProposal.federationUnit">Federation Unit</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('dependents')}>
                    <Translate contentKey="calcardApp.creditProposal.dependents">Dependents</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('income')}>
                    <Translate contentKey="calcardApp.creditProposal.income">Income</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('status')}>
                    <Translate contentKey="calcardApp.creditProposal.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('rejectionReason')}>
                    <Translate contentKey="calcardApp.creditProposal.rejectionReason">Rejection Reason</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('aprovedMin')}>
                    <Translate contentKey="calcardApp.creditProposal.aprovedMin">Aproved Min</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('aprovedMax')}>
                    <Translate contentKey="calcardApp.creditProposal.aprovedMax">Aproved Max</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('creationDate')}>
                    <Translate contentKey="calcardApp.creditProposal.creationDate">Creation Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('processingDate')}>
                    <Translate contentKey="calcardApp.creditProposal.processingDate">Processing Date</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {creditProposalList.map((creditProposal, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${creditProposal.id}`} color="link" size="sm">
                        {creditProposal.id}
                      </Button>
                    </td>
                    <td>{creditProposal.clientName}</td>
                    <td>{creditProposal.taxpayerId}</td>
                    <td>{creditProposal.clientAge}</td>
                    <td>
                      <Translate contentKey={`calcardApp.Gender.${creditProposal.gender}`} />
                    </td>
                    <td>
                      <Translate contentKey={`calcardApp.MaritalStatus.${creditProposal.maritalStatus}`} />
                    </td>
                    <td>
                      <Translate contentKey={`calcardApp.FederationUnit.${creditProposal.federationUnit}`} />
                    </td>
                    <td>{creditProposal.dependents}</td>
                    <td>{creditProposal.income}</td>
                    <td>
                      <Translate contentKey={`calcardApp.CreditProposalStatus.${creditProposal.status}`} />
                    </td>
                    <td>
                      <Translate contentKey={`calcardApp.RejectionReason.${creditProposal.rejectionReason}`} />
                    </td>
                    <td>{creditProposal.aprovedMin}</td>
                    <td>{creditProposal.aprovedMax}</td>
                    <td>
                      <TextFormat type="date" value={creditProposal.creationDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={creditProposal.processingDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${creditProposal.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${creditProposal.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${creditProposal.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ creditProposal }: IRootState) => ({
  creditProposalList: creditProposal.entities,
  totalItems: creditProposal.totalItems,
  links: creditProposal.links,
  entity: creditProposal.entity,
  updateSuccess: creditProposal.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CreditProposal);
