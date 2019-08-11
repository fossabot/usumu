import * as React from "react";
import {LinearProgress, Paper, Table, TableCell, TableHead, TableRow} from "@material-ui/core";

interface ISubscriberListProps {

}

interface ISubscriberListState {

}

export default function SubscriberList(props: ISubscriberListProps, state: ISubscriberListState) {
    return <Paper>
        <Table>
            <TableHead>
                <TableRow>
                    <TableCell>E-mail/Phone</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell align="right">Actions</TableCell>
                </TableRow>
            </TableHead>
        </Table>
        <LinearProgress />
    </Paper>
}