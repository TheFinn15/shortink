import React from 'react';
import './App.css';
import {Avatar, Card, CardContent, CardHeader, IconButton, Typography} from '@material-ui/core';
import {createStyles, makeStyles, Theme} from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    title: {
      margin: theme.spacing(15, 20, 0)
    }
  })
)


function UserPage() {
  const styles = useStyles()
  return (
    <div>
      <Typography variant="body2" className={styles.title}>
        jojojojojoj
      </Typography>
    </div>
  );
}

export default UserPage