import {ListItem, ListItemIcon, ListItemText} from "@material-ui/core";
import React, {ReactElement} from "react";
import {Link} from "@reach/router";

interface IMenuItemProps {
    icon: ReactElement,
    link: string,
    text: string
}

export default function(props: IMenuItemProps) {
    return <ListItem button component={Link} to={props.link}>
        <ListItemIcon>
            {props.icon}
        </ListItemIcon>
        <ListItemText primary={props.text} />
    </ListItem>
}