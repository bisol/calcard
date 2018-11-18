import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudSearchAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICreditProposal, defaultValue } from 'app/shared/model/credit-proposal.model';

export const ACTION_TYPES = {
  FETCH_CREDITPROPOSAL_LIST: 'creditProposal/FETCH_CREDITPROPOSAL_LIST',
  FETCH_CREDITPROPOSAL: 'creditProposal/FETCH_CREDITPROPOSAL',
  CREATE_CREDITPROPOSAL: 'creditProposal/CREATE_CREDITPROPOSAL',
  UPDATE_CREDITPROPOSAL: 'creditProposal/UPDATE_CREDITPROPOSAL',
  DELETE_CREDITPROPOSAL: 'creditProposal/DELETE_CREDITPROPOSAL',
  RESET: 'creditProposal/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICreditProposal>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CreditProposalState = Readonly<typeof initialState>;

// Reducer

export default (state: CreditProposalState = initialState, action): CreditProposalState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CREDITPROPOSAL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CREDITPROPOSAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CREDITPROPOSAL):
    case REQUEST(ACTION_TYPES.UPDATE_CREDITPROPOSAL):
    case REQUEST(ACTION_TYPES.DELETE_CREDITPROPOSAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CREDITPROPOSAL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CREDITPROPOSAL):
    case FAILURE(ACTION_TYPES.CREATE_CREDITPROPOSAL):
    case FAILURE(ACTION_TYPES.UPDATE_CREDITPROPOSAL):
    case FAILURE(ACTION_TYPES.DELETE_CREDITPROPOSAL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CREDITPROPOSAL_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CREDITPROPOSAL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CREDITPROPOSAL):
    case SUCCESS(ACTION_TYPES.UPDATE_CREDITPROPOSAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CREDITPROPOSAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/credit-proposals';

// Actions

export const getEntities: ICrudSearchAction<ICreditProposal> = (search, page, size, sort) => {
  const requestUrl = `${apiUrl}?cpf=${search}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CREDITPROPOSAL_LIST,
    payload: axios.get<ICreditProposal>(`${requestUrl}&cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICreditProposal> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CREDITPROPOSAL,
    payload: axios.get<ICreditProposal>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICreditProposal> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CREDITPROPOSAL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ICreditProposal> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CREDITPROPOSAL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICreditProposal> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CREDITPROPOSAL,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
