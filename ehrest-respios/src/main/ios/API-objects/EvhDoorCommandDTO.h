//
// EvhDoorCommandDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorCommandDTO
//
@interface EvhDoorCommandDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* serverKeyVer;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSString* cmdBody;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* cmdResp;

@property(nonatomic, copy) NSNumber* cmdId;

@property(nonatomic, copy) NSNumber* aclinkKeyVer;

@property(nonatomic, copy) NSNumber* cmdType;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* cmdSeq;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

