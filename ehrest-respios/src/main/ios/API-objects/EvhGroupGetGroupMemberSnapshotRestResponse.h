//
// EvhGroupGetGroupMemberSnapshotRestResponse.h
// generated at 2016-04-07 10:47:33 
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
