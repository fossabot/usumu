import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Select,
    TextField
} from "@material-ui/core";
import * as React from "react";
import {ChangeEvent, EventHandler, ReactNode} from "react";
import {string} from "prop-types";
import ValidatorChain from "./ValidatorChain";
import FieldValidatorChain from "./FieldValidatorChain";
import RequiredValidator from "./RequiredValidator";
import FormalEmailValidator from "./FormalEmailValidator";
import PhoneValidator from "./PhoneValidator";

interface IAddSubscriberDialogProps {
    open: boolean,
    handleClose: React.ReactEventHandler
}

interface IAddSubscriberDialogState {
    isValid: boolean,
    type: string,
    value: string
}

export default class AddSubscriberDialog extends React.Component<IAddSubscriberDialogProps, IAddSubscriberDialogState> {
    private emailValidators: FieldValidatorChain;
    private phoneValidators: FieldValidatorChain;

    constructor(props: IAddSubscriberDialogProps, state: IAddSubscriberDialogState) {
        super(props,state);
        this.state = {
            isValid: false,
            type: "",
            value: ""
        };

        this.emailValidators = new FieldValidatorChain();
        this.emailValidators.addValidator(new RequiredValidator());
        this.emailValidators.addValidator(new FormalEmailValidator());

        this.phoneValidators = new FieldValidatorChain();
        this.phoneValidators.addValidator(new RequiredValidator());
        this.phoneValidators.addValidator(new PhoneValidator());
    }

    isValid() {
        return (this.state.type == "EMAIL" && this.emailValidators.validate(this.state.value).length == 0) ||
            (this.state.type == "SMS" && this.phoneValidators.validate(this.state.value).length == 0)
    }

    onTypeChange = (ev: ChangeEvent<{name?:string|undefined,value:unknown}>, child: ReactNode) => {
        this.setState({
            type: ev.target.value as string
        })
    };

    onValueChange = (ev: ChangeEvent<HTMLInputElement>) => {
        let value = ev.target.value as string;



        this.setState({
            "value": value
        })
    };

    render = () => {
        return <Dialog open={this.props.open} onClose={this.props.handleClose} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">Add subscriber</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Please enter the subscriber information below. The subscriber will be sent a confirmation e-mail.
                </DialogContentText>
                <Select
                    native
                    name="type"
                    value={this.state.type}
                    onChange={this.onTypeChange}
                >
                    <option value=""/>
                    <option value="EMAIL">E-mail</option>
                    <option value="SMS">SMS</option>
                </Select>
                (state.type == "EMAIL"?
                <TextField
                    id="value"
                    label="E-mail"
                    type="email"
                    value={this.state.value}
                    onChange={this.onValueChange}
                    fullWidth
                />:(state.type == "SMS"?
                    <TextField
                        id="value"
                        label="Phone"
                        type="phone"
                        value={this.state.value}
                        onChange={this.onValueChange}
                        fullWidth
                    />:
                    <TextField
                        id="value"
                        label="E-mail/Phone"
                        type="email"
                        value={this.state.value}
                        onChange={this.onValueChange}
                        disabled
                        fullWidth
                    />
                ))
            </DialogContent>
            <DialogActions>
                <Button onClick={this.props.handleClose} color="secondary">
                    Cancel
                </Button>
                <Button onClick={this.props.handleClose} color="primary">
                    Subscribe
                </Button>
            </DialogActions>
        </Dialog>
    }
}