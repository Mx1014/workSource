//
// EvhGroupGetGroupMemberSnapshotRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhGroupMemberSnapshotDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupGetGroupMemberSnapshotRestResponse
//
@interface EvhGroupGetGroupMemberSnapshotRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGroupMemberSnapshotDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
