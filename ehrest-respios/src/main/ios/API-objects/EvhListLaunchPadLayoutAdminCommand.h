//
// EvhListLaunchPadLayoutAdminCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListLaunchPadLayoutAdminCommand
//
@interface EvhListLaunchPadLayoutAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

