import { createMuiTheme } from '@material-ui/core/styles';
import amber from '@material-ui/core/colors/amber';
import grey from '@material-ui/core/colors/grey';
import red from '@material-ui/core/colors/red';


export default createMuiTheme({
    palette: {
        primary: amber,
        secondary: grey,
        error: red
    }
});
