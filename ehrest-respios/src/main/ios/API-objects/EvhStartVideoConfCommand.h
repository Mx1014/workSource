//
// EvhStartVideoConfCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStartVideoConfCommand
//
@interface EvhStartVideoConfCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accountId;

@property(nonatomic, copy) NSString* password;

@property(nonatomic, copy) NSString* confName;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* duration;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

