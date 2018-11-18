import { Moment } from 'moment';

export const enum Gender {
  SINGLE = 'SINGLE',
  MARRIED = 'MARRIED',
  DIVORCED = 'DIVORCED',
  WIDOW = 'WIDOW',
  OTHER = 'OTHER'
}

export const enum MaritalStatus {
  SINGLE = 'SINGLE',
  MARRIED = 'MARRIED',
  DIVORCED = 'DIVORCED',
  WIDOW = 'WIDOW',
  OTHER = 'OTHER'
}

export const enum FederationUnit {
  AC = 'AC',
  AM = 'AM'
}

export const enum CreditProposalStatus {
  PROCESSING = 'PROCESSING',
  APROVED = 'APROVED',
  REJECTED = 'REJECTED'
}

export const enum RejectionReason {
  POLICY = 'POLICY',
  INCOME = 'INCOME'
}

export interface ICreditProposal {
  id?: number;
  clientName?: string;
  taxpayerId?: string;
  clientAge?: number;
  gender?: Gender;
  maritalStatus?: MaritalStatus;
  federationUnit?: FederationUnit;
  dependents?: number;
  income?: number;
  status?: CreditProposalStatus;
  rejectionReason?: RejectionReason;
  aprovedMin?: number;
  aprovedMax?: number;
  creationDate?: Moment;
  processingDate?: Moment;
}

export const defaultValue: Readonly<ICreditProposal> = {};
