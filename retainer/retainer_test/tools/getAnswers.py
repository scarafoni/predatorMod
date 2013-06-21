#!/usr/bin/env python
#

from boto.mturk.connection import ExternalQuestion
from boto.mturk.connection import MTurkConnection

import getopt
import sys
import random
import time
import MySQLdb

from datetime import timedelta


# Amazon Web Services Access Key
access_key='AKIAJO5TXEMWCGR4BWNQ'
# Amazon Web Services Secret Key
secret_key='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'
# HIT title
hit_title = 'Answer questions about a live video to help a blind requester'
# HIT description
hit_description = 'Watch a streaming video and provide answers to questions asked by the requester (requires sound).'
# HIT keywords
hit_keywords = 'questions answer help video'
# HIT URL
hit_url = ""
# Amazon Mechanical Turk host URL
host_url = 'mechanicalturk.amazonaws.com'
#host_url = 'mechanicalturk.sandbox.amazonaws.com'
#Number of HITs to compete
num_hits = 0
active_session="inactive"



def get_args():
	global num_hits
	global host_url
	global hit_url

	num_hits = int(sys.argv[1])
	#active_session = sys.argv[2]
	#vid = sys.argv[3]
	
	#print vid	
	if len(sys.argv) > 1 and sys.argv[2] == '-s':
		print "USING SANDBOX"
		host_url = 'mechanicalturk.sandbox.amazonaws.com'
	

	# sys.exit(99)

    # Make sure Amazon credentials and HIT details were specified.
	if not access_key or not secret_key or not hit_description or not hit_keywords or not hit_title:
		sys.exit(1)
	else:
		# HIT URL
		#hit_url = 'http://roc.cs.rochester.edu/ccvideo/workrouter?session=' + active_session + '&amp;video=' + vid
		hit_url = 'http://roc.cs.rochester.edu/ccvideo/workrouter/'


""" Create a new HIT and return the HITId. """
def create_hit(connection):
    question = ExternalQuestion(hit_url, 600)
    
    global hit_description
    global hit_keywords
    global hit_title
    
    # Create a new HIT.
    result_set = connection.create_hit(question=question,
                                       lifetime=timedelta(minutes=30),
                                       max_assignments=1,
                                       title=hit_title,
                                       description=hit_description,
                                       keywords=hit_keywords,
                                       reward=float(random.randint(10, 12)) / 100,
                                       duration=timedelta(minutes=30),
                                       approval_delay=timedelta(days=3))
    
    for result in result_set:
        return result.HITId


""" Return the status of the HIT associated with the supplied HITId. """
def hit_status(connection, hit_id):
    result_set = connection.get_hit(hit_id)
    
    for result in result_set:
        return result.HITStatus


""" Delete the HIT associated with the supplied HITId. """
def delete_hit(connection, hit_id):
    connection.expire_hit(hit_id)
    # Give the HIT a moment to expire.
    time.sleep(0.25)
    connection.dispose_hit(hit_id)


def main():
	get_args()
    
    # Connect to Mechanical Turk.
	print "Connecting to: " + host_url
	connection = MTurkConnection(aws_access_key_id=access_key,
                                 aws_secret_access_key=secret_key,
                                 host=host_url)
								 

	global num_hits
	hits = []
	
    #Query the database for the minimum number of answers for a any question. 
	db = MySQLdb.connect(host='localhost', user='root', passwd='borkborkbork', db='segmentation')
	cursor = db.cursor()
    #count = cursor.execute("SELECT COUNT(segment) FROM segments WHERE session=" + "testing").fetchone()[0]
	count = cursor.execute("SELECT COUNT(segment) FROM segments WHERE session='" + active_session + "';")
	while (num_hits > 0):
		print "Count so far:"
		print num_hits
		while len(hits) < 50:
			print "Posting new HIT..."
			hits.append((create_hit(connection), int(time.time())))
			# Give the HIT a moment to post.
			time.sleep(3)
		
		for hit in hits[:]:
			# Remove completed HITs.
			if hit_status(connection, hit[0]) == 'Reviewable':
				print time.strftime('HIT completed at %H:%M:%S' , time.localtime())
				hits.remove(hit)
				num_hits -= 1
				continue

			# Remove old HITs.
			if hit_status(connection, hit[0]) == 'Assignable' and hit[1] + 300 < int(time.time()):
				print "Removing old HIT."
				delete_hit(connection, hit[0])
				hits.remove(hit)
				#num_hits -= 1
				continue
		
    		count = cursor.execute("SELECT COUNT(segment) FROM segments WHERE session='" + active_session + "';")
		
	
    # Cleanup any leftover HITs.
	print "Deleting Remaining HITs..."

	for hit in hits:
		print str(hit[0])
		if hit_status(connection, hit[0]) == 'Assignable' or hit_status(connection, hit[0]) == 'Reviewable':
			delete_hit(connection, hit[0])
	
	
    


if __name__ == '__main__':
	main()
