//
// EvhDoorAccessQRKeyDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessQRKeyDTO
//
@interface EvhDoorAccessQRKeyDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* doorGroupId;

@property(nonatomic, copy) NSString* doorName;

@property(nonatomic, copy) NSString* qrCodeKey;

@property(nonatomic, copy) NSString* qrDriver;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTimeMs;

@property(nonatomic, copy) NSNumber* expireTimeMs;

@property(nonatomic, copy) NSNumber* status;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* hardwares;

@property(nonatomic, copy) NSString* extra;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

