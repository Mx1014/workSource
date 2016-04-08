//
// EvhUpdatePersistServerCommand.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePersistServerCommand
//
@interface EvhUpdatePersistServerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* masterId;

@property(nonatomic, copy) NSString* addressUri;

@property(nonatomic, copy) NSNumber* addressPort;

@property(nonatomic, copy) NSNumber* serverType;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* configTag;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

