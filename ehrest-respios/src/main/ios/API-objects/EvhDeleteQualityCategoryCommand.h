//
// EvhDeleteQualityCategoryCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteQualityCategoryCommand
//
@interface EvhDeleteQualityCategoryCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* categoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

