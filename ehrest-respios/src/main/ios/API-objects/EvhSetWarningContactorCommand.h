//
// EvhSetWarningContactorCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetWarningContactorCommand
//
@interface EvhSetWarningContactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* email;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

