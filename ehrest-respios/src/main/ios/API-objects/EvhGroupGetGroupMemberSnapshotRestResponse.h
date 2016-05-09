//
// EvhGroupGetGroupMemberSnapshotRestResponse.h
// generated at 2016-04-29 18:56:03 
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
