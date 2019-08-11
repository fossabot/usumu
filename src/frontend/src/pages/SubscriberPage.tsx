import * as React from "react";
import {RouteComponentProps} from "@reach/router";
import SubscriberService from "../services/SubscriberService";
import SubscriberList from "../components/SubscriberList";
import {Fab, makeStyles, Tooltip} from "@material-ui/core";
import AddIcon from '@material-ui/icons/Add';

interface ISubscriberPageProps extends RouteComponentProps {
    subscriberService: SubscriberService
}

interface ISubscriberPageState {
}

const useStyles = makeStyles(theme => ({
    fab: {
        position: 'absolute',
        bottom: theme.spacing(2),
        right: theme.spacing(2),
    },
}));


export default function(props: ISubscriberPageProps, state: ISubscriberPageState) {
    const classes = useStyles();
    return <div>
        <SubscriberList />
        <Tooltip title="Add subscriber" className={classes.fab}>
            <Fab color="primary" aria-label="add">
                <AddIcon />
            </Fab>
        </Tooltip>
    </div>
}