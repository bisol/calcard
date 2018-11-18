import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CreditProposal from './credit-proposal';
import CreditProposalDetail from './credit-proposal-detail';
import CreditProposalUpdate from './credit-proposal-update';
import CreditProposalDeleteDialog from './credit-proposal-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CreditProposalUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CreditProposalUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CreditProposalDetail} />
      <ErrorBoundaryRoute path={match.url} component={CreditProposal} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CreditProposalDeleteDialog} />
  </>
);

export default Routes;
