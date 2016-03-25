//
// EvhGroupGetGroupMemberSnapshotRestResponse.h
// generated at 2016-03-25 09:26:44 
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
