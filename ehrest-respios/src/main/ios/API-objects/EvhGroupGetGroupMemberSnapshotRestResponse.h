//
// EvhGroupGetGroupMemberSnapshotRestResponse.h
// generated at 2016-03-31 11:07:27 
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
