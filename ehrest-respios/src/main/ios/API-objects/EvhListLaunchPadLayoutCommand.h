//
// EvhListLaunchPadLayoutCommand.h
// generated at 2016-03-25 11:43:33 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListLaunchPadLayoutCommand
//
@interface EvhListLaunchPadLayoutCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

