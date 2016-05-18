//
// EvhCreateAclinkFirmwareCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateAclinkFirmwareCommand
//
@interface EvhCreateAclinkFirmwareCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* infoUrl;

@property(nonatomic, copy) NSNumber* checksum;

@property(nonatomic, copy) NSString* md5sum;

@property(nonatomic, copy) NSString* downloadUrl;

@property(nonatomic, copy) NSNumber* major;

@property(nonatomic, copy) NSNumber* minor;

@property(nonatomic, copy) NSNumber* revision;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

