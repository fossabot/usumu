import React from 'react';
import theme from "./theme";
import {ThemeProvider} from '@material-ui/styles';
import Layout from "./Layout";
import SubscriberService from "./services/SubscriberService";
import SubscriberPage from "./pages/SubscriberPage";
import {Router} from "@reach/router";

const subscriberService = new SubscriberService();

const App: React.FC = () => {
    return (
        <ThemeProvider theme={theme}>
            <Layout>
                <Router>
                    <SubscriberPage path="/subscribers" subscriberService={subscriberService} />
                </Router>
            </Layout>
        </ThemeProvider>
    );
};

export default App;
