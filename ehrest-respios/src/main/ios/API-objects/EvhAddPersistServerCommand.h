//
// EvhAddPersistServerCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddPersistServerCommand
//
@interface EvhAddPersistServerCommand
    : NSObject<EvhJsonSerializable>


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

