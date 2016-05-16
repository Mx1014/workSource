//
// EvhAclinkFirmwareDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkFirmwareDTO
//
@interface EvhAclinkFirmwareDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* infoUrl;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* major;

@property(nonatomic, copy) NSNumber* firmwareType;

@property(nonatomic, copy) NSNumber* checksum;

@property(nonatomic, copy) NSString* md5sum;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* downloadUrl;

@property(nonatomic, copy) NSNumber* creatorId;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* minor;

@property(nonatomic, copy) NSNumber* revision;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

